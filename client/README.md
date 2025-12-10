# Patient Service - Client

React frontend application for patient management.

## Tech Stack

- **React 19** with **TypeScript**
- **Vite** - Build tool
- **Material-UI (MUI)** - UI components
- **Axios** - HTTP client

## Prerequisites

- Node.js 18+
- Yarn (package manager)

## Setup

1. Install dependencies:

   ```bash
   yarn install
   ```

2. Start the development server:
   ```bash
   yarn dev
   ```

The application will be available at `http://localhost:5173` (or the port Vite assigns).

## Build

To build for production:

```bash
yarn build
```

The build output will be in the `dist` directory.

## API Configuration

The client connects to the backend API. Make sure the backend server is running on `http://localhost:8083` or update the API base URL in `src/services/apiClient.ts`.
