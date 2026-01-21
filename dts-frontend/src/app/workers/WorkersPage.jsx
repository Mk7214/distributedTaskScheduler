import React, { useEffect, useState } from "react"
import { NodeCard } from "@/components/NodeCard"
import { Box } from "lucide-react"
import { api } from "@/lib/api"
import { toast } from "sonner"
import { useNavigate } from "react-router"
import { Button } from "@/components/ui/button"

import { IconPlus } from "@tabler/icons-react"

export function WorkersPage() {
    const navigate = useNavigate();
    const [nodes, setNodes] = useState([
        { id: "worker-1", name: "Worker Node 1", status: "stopped", description: "Standard compute node" }
    ])

    const handleAddNode = async () => {
        try {
            const res = await api.worker.start();
            const workerId = res.workerId || `worker-${Date.now()}`;
            toast.success(`Worker ${workerId} started`);
            const newNode = {
                id: workerId,
                name: `Worker Node ${nodes.length + 1}`,
                status: "running",
                description: `Dynamically started worker`
            };
            setNodes(prev => [...prev, newNode]);
        } catch (error) {
            toast.error(error.message);
        }
    }

    const handleStartNode = async (id) => {
        try {
            const res = await api.worker.start();
            const workerId = res.workerId || id;
            toast.success(`Worker ${workerId} started`);
            setNodes(prev => prev.map(n => n.id === id ? { ...n, id: workerId, status: "running" } : n));
        } catch (error) {
            toast.error(error.message);
        }
    }

    const handleStopNode = async (id) => {
        try {
            const res = await api.worker.stop(id);
            toast.success(res || "Worker stopped");
            setNodes(prev => prev.map(n => n.id === id ? { ...n, status: "stopped" } : n));
        } catch (error) {
            toast.error(error.message);
        }
    }

    return (
        <div className="@container/main flex flex-1 flex-col gap-2">
            <div className="flex flex-col gap-8 py-6 px-4 lg:px-8 max-w-7xl mx-auto w-full">
                <section className="space-y-8">
                    <div className="flex items-end justify-between pb-4 border-b border-white/5">
                        <div className="space-y-1">
                            <div className="flex items-center gap-2">
                                <Box className="size-5 text-primary" />
                                <span className="text-[10px] font-extrabold uppercase tracking-[0.2em] text-zinc-500">Infrastructure</span>
                            </div>
                            <h1 className="text-4xl font-extrabold tracking-tighter text-white">Worker Nodes</h1>
                        </div>
                        <Button
                            onClick={handleAddNode}
                            className="bg-primary hover:bg-primary/90 text-primary-foreground font-extrabold tracking-tight px-6 h-11 rounded-xl shadow-[0_0_20px_rgba(var(--primary),0.3)] transition-all hover:scale-105"
                        >
                            <IconPlus className="mr-2 size-5 stroke-[3]" />
                            START NEW WORKER
                        </Button>
                    </div>

                    <div className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
                        {nodes.map(node => (
                            <NodeCard
                                key={node.id}
                                {...node}
                                type="worker"
                                onStart={() => handleStartNode(node.id)}
                                onStop={(id) => handleStopNode(id)}
                                onLogs={() => navigate(`/logs/worker`)}
                            />
                        ))}
                    </div>
                </section>
            </div>
        </div>
    )
}
