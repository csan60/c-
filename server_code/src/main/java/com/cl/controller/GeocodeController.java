package com.cl.controller;

import com.cl.config.GeocodeConfig;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/geocode")
@CrossOrigin
public class GeocodeController {

    @Autowired
    private GeocodeConfig geocodeConfig;

    @Autowired(required = false)
    private RestTemplate restTemplate;

    @GetMapping("/config")
    public R config() {
        Map<String, Object> data = new HashMap<>();
        data.put("provider", geocodeConfig.getProvider());
        String qq = geocodeConfig.getQqKey();
        String amap = geocodeConfig.getAmapKey();
        data.put("qqConfigured", qq != null && !qq.isEmpty());
        data.put("amapConfigured", amap != null && !amap.isEmpty());
        return R.ok().put("data", data);
    }

    @GetMapping("/reverse")
    public R reverse(@RequestParam("lat") double lat, @RequestParam("lng") double lng,
                     @RequestParam(value = "provider", required = false) String provider) {
        try {
            String use = (provider == null || provider.trim().isEmpty())
                    ? geocodeConfig.getProvider()
                    : provider.toLowerCase();
            String address = "";

            if ("amap".equals(use)) {
                // ======== 高德 ========
                String key = geocodeConfig.getAmapKey();
                if (key == null || key.isEmpty()) return R.error("AMap Key未配置");

                String url = "https://restapi.amap.com/v3/geocode/regeo?key=" + enc(key)
                        // 高德用的是 lng,lat
                        + "&location=" + enc(lng + "," + lat)
                        + "&extensions=all&output=json";
                Map res = restTemplate.getForObject(url, Map.class);
                if (res != null) {
                    Object status = res.get("status");
                    if ("1".equals(String.valueOf(status))) {
                        Object regeocode = res.get("regeocode");
                        if (regeocode instanceof Map) {
                            Map rg = (Map) regeocode;
                            Object fmt = rg.get("formatted_address");
                            if (fmt != null) {
                                address = String.valueOf(fmt);
                            } else {
                                // 兜底：拼省市区
                                Map comp = (Map) rg.get("addressComponent");
                                if (comp != null) {
                                    StringBuilder sb = new StringBuilder();
                                    append(sb, comp.get("province"));
                                    append(sb, comp.get("city"));
                                    append(sb, comp.get("district"));
                                    append(sb, comp.get("township"));
                                    address = sb.toString();
                                }
                            }
                        }
                    } else {
                        return R.error("AMap逆地理失败: " + res.get("info"));
                    }
                }
            } else {
                // ======== 腾讯地图 ========
                String key = geocodeConfig.getQqKey();
                if (key == null || key.isEmpty()) return R.error("QQ Map Key未配置");

                String url = "https://apis.map.qq.com/ws/geocoder/v1/?location=" + enc(lat + "," + lng)
                        + "&key=" + enc(key)
                        + "&get_poi=1"
                        // 改成更适合“发位置”的策略
                        + "&poi_options=" + enc("address_format=short;policy=5")
                        + "&output=json";
                Map res = restTemplate.getForObject(url, Map.class);
                if (res != null) {
                    Object status = res.get("status");
                    if (status instanceof Number && ((Number) status).intValue() != 0) {
                        Object msg = res.get("message");
                        return R.error(msg == null ? "逆地理解析失败" : String.valueOf(msg));
                    }
                    Object result = res.get("result");
                    if (result instanceof Map) {
                        Map r = (Map) result;

                        // 1) 优先用推荐地址
                        Object fa = r.get("formatted_addresses");
                        if (fa instanceof Map) {
                            Object recommend = ((Map) fa).get("recommend");
                            if (recommend != null) {
                                address = String.valueOf(recommend);
                            }
                        }

                        // 2) 兜底用 address
                        if (address == null || address.isEmpty()) {
                            Object addr = r.get("address");
                            if (addr != null) {
                                address = String.valueOf(addr);
                            }
                        }

                        // ★ 不再从 pois 里筛“学校/园区”，避免文字看起来偏得离谱
                    }
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("address", address);
            data.put("provider", use);
            return R.ok().put("data", data);
        } catch (Exception e) {
            return R.error("解析异常: " + e.getMessage());
        }
    }

    private void append(StringBuilder sb, Object part) {
        if (part != null) sb.append(part);
    }


    private String enc(String s) throws UnsupportedEncodingException { return URLEncoder.encode(s, String.valueOf(StandardCharsets.UTF_8)); }
    @GetMapping("/nearby")
    public R nearby(@RequestParam("lat") double lat,
                    @RequestParam("lng") double lng,
                    @RequestParam(value = "provider", required = false) String provider,
                    @RequestParam(value = "radius", required = false) Integer radius,
                    @RequestParam(value = "keyword", required = false) String keyword) {
        try {
            String use = (provider == null || provider.trim().isEmpty()) ? geocodeConfig.getProvider() : provider.toLowerCase();
            int r = (radius == null || radius <= 0) ? 3000 : radius;
            String kw = (keyword == null || keyword.trim().isEmpty()) ? "学院|大学|学校|产业学院|科技园|园区" : keyword;
            String name = "";
            String address = "";
            double distPick = Double.MAX_VALUE;
            if ("amap".equals(use)) {
                String key = geocodeConfig.getAmapKey();
                if (key == null || key.isEmpty()) return R.error("AMap Key未配置");
                String url = "https://restapi.amap.com/v3/place/around?key=" + enc(key)
                        + "&location=" + enc(lng + "," + lat)
                        + "&radius=" + enc(String.valueOf(r))
                        + "&types=" + enc("高等院校|学校|教育科研|产业园区|科技园区")
                        + "&keywords=" + enc(kw)
                        + "&output=json";
                Map res = restTemplate.getForObject(url, Map.class);
                if (res != null) {
                    Object pois = res.get("pois");
                    if (pois instanceof java.util.List) {
                        for (Object o : (java.util.List) pois) {
                            if (!(o instanceof Map)) continue;
                            Map p = (Map) o;
                            Object n = p.get("name");
                            Object adr = p.get("address");
                            Object dObj = p.get("distance");
                            double d = dObj instanceof Number ? ((Number) dObj).doubleValue() : 0.0;
                            if (n != null && (d <= r) && d < distPick) {
                                distPick = d; name = String.valueOf(n); address = adr == null ? "" : String.valueOf(adr);
                            }
                        }
                    }
                }
            } else {
                String key = geocodeConfig.getQqKey();
                if (key == null || key.isEmpty()) return R.error("QQ Map Key未配置");
                String url = "https://apis.map.qq.com/ws/place/v1/search?boundary=" + enc("nearby(" + lat + "," + lng + "," + r + ")")
                        + "&keyword=" + enc(kw)
                        + "&key=" + enc(key) + "&page_size=20";
                Map res = restTemplate.getForObject(url, Map.class);
                if (res != null) {
                    Object status = res.get("status");
                    if (status instanceof Number && ((Number) status).intValue() != 0) {
                        Object msg = res.get("message");
                        return R.error(msg == null ? "附近POI查询失败" : String.valueOf(msg));
                    }
                    Object data = res.get("data");
                    if (data instanceof java.util.List) {
                        for (Object o : (java.util.List) data) {
                            if (!(o instanceof Map)) continue;
                            Map p = (Map) o;
                            Object t = p.get("title");
                            Object adr = p.get("address");
                            Object dObj = p.get("_distance");
                            double d = dObj instanceof Number ? ((Number) dObj).doubleValue() : 0.0;
                            if (t != null && (d <= r) && d < distPick) {
                                distPick = d; name = String.valueOf(t); address = adr == null ? "" : String.valueOf(adr);
                            }
                        }
                    }
                }
            }
            Map<String, Object> data = new HashMap<>();
            data.put("name", name);
            data.put("address", address);
            data.put("distance", distPick == Double.MAX_VALUE ? null : distPick);
            data.put("provider", use);
            return R.ok().put("data", data);
        } catch (Exception e) {
            return R.error("查询异常: " + e.getMessage());
        }
    }
}