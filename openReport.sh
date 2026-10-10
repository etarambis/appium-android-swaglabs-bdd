#!/bin/bash
# Abre el reporte HTML de Cucumber en el navegador (Windows/Git Bash, macOS o Linux)
REPORT="target/cucumber-html-reports/overview-features.html"

case "$(uname -s)" in
  Darwin*) open "$REPORT" ;;
  Linux*)  xdg-open "$REPORT" ;;
  *)       start "$REPORT" ;;
esac
