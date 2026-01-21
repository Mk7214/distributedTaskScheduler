import { Routes, Route, Navigate } from "react-router"
import { Layout } from "./components/Layout"
import { DashboardPage } from "./app/dashboard/DashboardPage"
import { SchedulersPage } from "./app/schedulers/SchedulersPage"
import { WorkersPage } from "./app/workers/WorkersPage"
import { LogsPage } from "./app/logs/LogsPage"

function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/schedulers" element={<SchedulersPage />} />
        <Route path="/workers" element={<WorkersPage />} />
        <Route path="/logs/:type" element={<LogsPage />} />
      </Routes>
    </Layout>
  )
}

export default App
