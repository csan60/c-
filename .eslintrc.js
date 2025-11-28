module.exports = {
  root: true,
  env: {
    browser: true,
    es2021: true,
    node: true,
  },
  extends: [
    'eslint:recommended',
    'plugin:vue/recommended',
    'plugin:prettier/recommended',
  ],
  parserOptions: {
    ecmaVersion: 2021,
    sourceType: 'module',
  },
  globals: {
    uni: 'readonly',
    wx: 'readonly',
    plus: 'readonly',
    getApp: 'readonly',
    getCurrentPages: 'readonly',
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'vue/multi-word-component-names': 'off',
    'vue/no-v-html': 'off',
    'no-unused-vars': ['warn', { argsIgnorePattern: '^_' }],
    'prettier/prettier': [
      'error',
      {
        endOfLine: 'auto',
      },
    ],
  },
};
