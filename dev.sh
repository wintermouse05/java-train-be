#!/bin/bash
# Chay backend Spring Boot trong Docker development mode
set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

COMPOSE_FILE="docker-compose.yml"

show_usage() {
    echo -e "${BLUE}Usage: $0 [command]${NC}"
    echo ""
    echo "  start     Khoi dong backend (app + mysql + adminer)"
    echo "  stop      Dung backend"
    echo "  restart   Khoi dong lai"
    echo "  logs      Xem logs (realtime)"
    echo "  status    Kiem tra trang thai"
    echo "  shell     Truy cap shell container student-app"
    echo "  clean     Don dep toan bo (container, volume, image)"
}

case "${1:-}" in
    start)
        echo -e "${YELLOW}Khoi dong backend services...${NC}"
        docker-compose -f "$COMPOSE_FILE" up -d --build
        echo -e "${GREEN}Backend API: http://localhost:8080${NC}"
        echo -e "${GREEN}Adminer:     http://localhost:8081${NC}"
        ;;
    stop)
        docker-compose -f "$COMPOSE_FILE" down
        echo -e "${GREEN}Backend da dung.${NC}"
        ;;
    restart)
        docker-compose -f "$COMPOSE_FILE" restart
        echo -e "${GREEN}Backend da khoi dong lai.${NC}"
        ;;
    logs)
        docker-compose -f "$COMPOSE_FILE" logs -f
        ;;
    status)
        docker-compose -f "$COMPOSE_FILE" ps
        echo ""
        docker stats --no-stream --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}" 2>/dev/null || true
        ;;
    shell)
        docker-compose -f "$COMPOSE_FILE" exec student-app sh
        ;;
    clean)
        echo -e "${YELLOW}Don dep Docker resources...${NC}"
        docker-compose -f "$COMPOSE_FILE" down -v --remove-orphans
        docker image prune -f
        echo -e "${GREEN}Don dep hoan tat.${NC}"
        ;;
    *)
        show_usage
        exit 1
        ;;
esac
