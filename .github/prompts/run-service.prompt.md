---
mode: agent
---

Execute all the tests for ${input:serviceName} service and generate a test report.

Ensure you ask the user what service they want to generate a test report for.

# Test Report Output

Create a comprehensive test report for the ${input:serviceName} service, including details on the number of tests executed, passed, failed, and skipped.

## Service: ${input:serviceName}
## Test Results
- **Total Tests**: ${input:totalTests}
- **Passed**: ${input:passedTests}
- **Failed**: ${input:failedTests}
- **Skipped**: ${input:skippedTests}

Format the report in Markdown for easy readability and integration into documentation.

Put the test report in a file named `test-report-${input:serviceName}.md` in the root directory of the service under `../docs/test-reports/`
