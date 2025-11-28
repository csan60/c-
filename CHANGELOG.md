# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Initial project setup and configuration
- `.gitignore` file to exclude IDE folders, build outputs, and temporary files
- Comprehensive `README.md` with project overview, features, and setup instructions
- `package.json` for dependency management and npm scripts
- `.editorconfig` for consistent code formatting across editors
- `.eslintrc.js` for code quality and linting rules
- `.prettierrc` for code formatting standards
- `.npmrc` for npm configuration
- Environment configuration files (`.env.example`, `.env.development`, `.env.production`)
- Centralized configuration system (`config/index.js`, `api/config.js`)
- Application constants file (`constants/index.js`) for shared values
- Logger utility (`utils/logger.js`) for structured logging
- Contributing guidelines (`CONTRIBUTING.md`)
- Code of Conduct (`CODE_OF_CONDUCT.md`)
- This changelog file

### Changed
- Refactored `api/base.js` to use centralized configuration
- Enhanced `api/http.js` with:
  - Better error handling and logging
  - Use of constants for storage keys and response codes
  - Improved token management
  - Cleaner unauthorized (401) handling with storage cleanup
  - Timeout configuration support
  - Modern ES6+ syntax and code style
  
### Improved
- Code organization and structure
- Error handling and user feedback
- Development experience with better tooling
- Documentation and onboarding process
- Separation of concerns (config, constants, utilities)

### Fixed
- Missing `.gitignore` causing IDE and build files to be tracked
- Hardcoded API URLs without environment configuration
- Inconsistent code style throughout the project
- Poor error handling in HTTP requests
- Missing project documentation

## [1.0.0] - Initial Release

### Features
- User authentication (login, register, password reset, password change)
- Mental health content management (articles, collections, comments)
- Psychological counselor system (listings, appointments, chat)
- Online assessment system (test papers, questions, exam records)
- News and announcements
- Forum for discussions
- AI consultation placeholder
- Personal center with user information
- Role-based access control (Admin and User roles)
- Cross-platform support (H5, WeChat Mini Program, App)

---

## Version Guidelines

- **Major version (X.0.0)**: Breaking changes
- **Minor version (0.X.0)**: New features, backward compatible
- **Patch version (0.0.X)**: Bug fixes, backward compatible

## Change Categories

- **Added**: New features
- **Changed**: Changes in existing functionality
- **Deprecated**: Soon-to-be removed features
- **Removed**: Removed features
- **Fixed**: Bug fixes
- **Security**: Security updates
