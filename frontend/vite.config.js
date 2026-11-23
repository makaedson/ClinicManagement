import { fileURLToPath } from "node:url";
import { dirname, resolve } from "node:path";
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

const frontendDirectory = dirname(fileURLToPath(import.meta.url));

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api": "http://localhost:8080"
    }
  },
  build: {
    outDir: resolve(frontendDirectory, "../src/main/resources/static"),
    emptyOutDir: true
  }
});
