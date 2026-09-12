import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  output: "standalone",
  allowedDevOrigins: [
    "*.trycloudflare.com",
    "yrs-jvc-minutes-substance.trycloudflare.com",
    "localhost:3000",
    "localhost:3001",
  ],
};

export default nextConfig;
