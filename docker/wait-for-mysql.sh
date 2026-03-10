#!/bin/sh
set -e

HOST="$1"
USER="$2"
PASS="$3"
DB="$4"
shift 4

if [ -z "$HOST" ] || [ -z "$USER" ] || [ -z "$PASS" ] || [ -z "$DB" ]; then
  echo "Usage: wait-for-mysql.sh <host> <user> <password> <database> <command...>"
  exit 1
fi

echo "Waiting for MySQL at ${HOST}:3306 (db=${DB})..."
until mysql -h "$HOST" -u"$USER" -p"$PASS" -e "SELECT 1" "$DB" >/dev/null 2>&1; do
  echo "MySQL is not ready yet. Retrying in 2s..."
  sleep 2
done

echo "MySQL is ready. Starting application..."
exec "$@"
