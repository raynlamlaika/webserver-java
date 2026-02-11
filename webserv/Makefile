.PHONY: all build clean test run help fclean re package watch dep


dep:
	@echo "[DEP] Installing system dependencies..."
	@if command -v yum >/dev/null 2>&1; then \
	    yum update -y && yum install -y inotify-tools || yum install -y epel-release && yum install -y inotify-tools; \
	elif command -v microdnf >/dev/null 2>&1; then \
	    microdnf update -y && microdnf install -y epel-release && microdnf install -y inotify-tools; \
	elif command -v apt-get >/dev/null 2>&1; then \
	    apt-get update && apt-get install -y inotify-tools; \
	fi
	@echo "[DEP] Dependencies installed"

	
# Default target
all: build

# Build the project
build:
	@echo "[BUILD] Compiling Java project..."
	./mvnw clean compile

# Run tests
test:
	@echo "[TEST] Running unit tests..."
	./mvnw test

# Package the application (creates JAR)
package: build
	@echo "[PACKAGE] Creating JAR file..."
	./mvnw package

# Run the application (with optional CONFIG_FILE argument)
run: package
	@echo "[RUN] Starting application..."
	@if [ -z "$(CONFIG_FILE)" ]; then \
		java -jar target/webserv.jar; \
	else \
		java -jar target/webserv.jar $(CONFIG_FILE); \
	fi

dev:
	./mvnw compile -q
	./mvnw exec:java \
		-Dexec.mainClass=com.example.app.App \
		-Dexec.args="$(CONFIG_FILE)"
# Watch and auto-rebuild on file changes
watch:
	@echo "[WATCH] Monitoring for file changes..."
	@while true; do \
		inotifywait -e modify -r src/ 2>/dev/null && make build; \
	done

# Clean build artifacts
clean:
	@echo "[CLEAN] Removing build artifacts..."
	./mvnw clean


# Full clean (like fclean in 42)
fclean: clean
	@rm -rf target/
	@echo "[FCLEAN] Removed all build files"

# Rebuild (clean + build)
re: fclean build

# Display help
help:
	@echo "=== Webserv Java Build System ==="
	@echo "Usage: make [target]"
	@echo ""
	@echo "Targets:"
	@echo "  build              - Compile the project"
	@echo "  test               - Run unit tests"
	@echo "  package            - Package into JAR"
	@echo "  run                - Build and run the app (prompts for config)"
	@echo "  run CONFIG_FILE=X  - Build and run with specific config file"
	@echo "  watch              - Auto-rebuild on file changes (requires inotify-tools)"
	@echo "  clean              - Remove build artifacts"
	@echo "  fclean             - Full clean (like 42 school)"
	@echo "  re                 - Rebuild everything"
	@echo "  help               - Show this message"

