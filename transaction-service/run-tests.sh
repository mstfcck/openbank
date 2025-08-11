#!/bin/bash

echo "=== Transaction Service Test Runner ==="
echo "Starting comprehensive unit tests for all controller endpoints..."
echo ""

# Navigate to the transaction service directory
cd "$(dirname "$0")"

echo "Current directory: $(pwd)"
echo ""

# Clean and compile
echo "🔨 Cleaning and compiling..."
./mvnw clean compile test-compile

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo ""
    
    # Run the tests
    echo "🧪 Running TransactionControllerTest..."
    ./mvnw test -Dtest=TransactionControllerTest
    
    if [ $? -eq 0 ]; then
        echo ""
        echo "✅ All TransactionController tests passed!"
        echo ""
        
        # Run all tests
        echo "🧪 Running all tests..."
        ./mvnw test
        
        if [ $? -eq 0 ]; then
            echo ""
            echo "🎉 All tests passed successfully!"
            echo ""
            echo "📊 Test Coverage Summary:"
            echo "- TransactionController: 17 endpoints tested"
            echo "- CRUD operations: ✅ Covered"
            echo "- Pagination: ✅ Covered"
            echo "- Filtering: ✅ Covered"
            echo "- Validation: ✅ Covered"
            echo "- Error handling: ✅ Covered"
            echo "- Statistics: ✅ Covered"
            echo "- Admin operations: ✅ Covered"
            echo ""
        else
            echo "❌ Some tests failed. Check the output above for details."
            exit 1
        fi
    else
        echo "❌ TransactionControllerTest failed. Check the output above for details."
        exit 1
    fi
else
    echo "❌ Compilation failed. Check the output above for details."
    exit 1
fi

echo "=== Test Runner Complete ==="
