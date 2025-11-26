package com.cl.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.InterventionExecutionDao;
import com.cl.entity.InterventionExecution;
import com.cl.service.InterventionExecutionService;
import org.springframework.stereotype.Service;

@Service
public class InterventionExecutionServiceImpl extends ServiceImpl<InterventionExecutionDao, InterventionExecution> implements InterventionExecutionService {
}