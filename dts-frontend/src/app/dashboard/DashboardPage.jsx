import React, { useEffect, useState } from "react"
import { TaskCreationSheet } from "@/components/TaskCreationSheet"
import { TasksTable } from "@/components/TasksTable"
import { ListChecks, Ghost } from "lucide-react"
import { api } from "@/lib/api"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"

export function DashboardPage() {
    const [completedTasks, setCompletedTasks] = useState([])
    const [deadTasks, setDeadTasks] = useState([])

    const fetchTasks = async () => {
        try {
            const completed = await api.tasks.getCompleted();
            const dead = await api.tasks.getDead();
            setCompletedTasks(Array.isArray(completed) ? completed : []);
            setDeadTasks(Array.isArray(dead) ? dead : []);
        } catch (error) {
            console.error("Failed to fetch tasks:", error);
        }
    }

    useEffect(() => {
        fetchTasks();
        const interval = setInterval(fetchTasks, 10000);
        return () => clearInterval(interval);
    }, [])

    return (
        <div className="@container/main flex flex-1 flex-col gap-2">
            <div className="flex flex-col gap-8 py-8 px-4 lg:px-8 max-w-7xl mx-auto w-full">

                {/* Primary Section: Tasks */}
                <section className="space-y-8">
                    <div className="flex items-end justify-between pb-4 border-b border-white/5">
                        <div className="space-y-1">
                            <div className="flex items-center gap-2">
                                <ListChecks className="size-5 text-primary" />
                                <span className="text-[10px] font-extrabold uppercase tracking-[0.2em] text-zinc-500">Operation Center</span>
                            </div>
                            <h1 className="text-4xl font-extrabold tracking-tighter text-white">Task Management</h1>
                        </div>
                        <TaskCreationSheet onTaskCreated={fetchTasks} />
                    </div>

                    <div className="rounded-2xl bg-zinc-900/40 border-none shadow-2xl p-2 backdrop-blur-sm overflow-hidden">
                        <Tabs defaultValue="completed" className="w-full">
                            <TabsList className="w-full justify-start p-2 bg-zinc-950/40 border-b border-white/5 rounded-none mb-6">
                                <TabsTrigger value="completed" className="gap-2 data-[state=active]:bg-primary data-[state=active]:text-primary-foreground font-extrabold tracking-tight px-8 h-10 transition-all rounded-lg">
                                    <ListChecks className="size-4" />
                                    COMPLETED
                                </TabsTrigger>
                                <TabsTrigger value="dead" className="gap-2 data-[state=active]:bg-red-500 data-[state=active]:text-white font-extrabold tracking-tight px-8 h-10 transition-all rounded-lg ml-2">
                                    <Ghost className="size-4" />
                                    DEAD TASKS
                                </TabsTrigger>
                            </TabsList>
                            <div className="px-6 pb-8">
                                <TabsContent value="completed" className="mt-0 border-none shadow-none animate-in fade-in slide-in-from-bottom-2 duration-300">
                                    <TasksTable title="Execution Success History" tasks={completedTasks} />
                                </TabsContent>
                                <TabsContent value="dead" className="mt-0 border-none shadow-none animate-in fade-in slide-in-from-bottom-2 duration-300">
                                    <TasksTable title="Failed Task Recovery" tasks={deadTasks} />
                                </TabsContent>
                            </div>
                        </Tabs>
                    </div>
                </section>
            </div>
        </div>
    )
}
