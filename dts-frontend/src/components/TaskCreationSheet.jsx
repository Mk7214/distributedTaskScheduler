import React, { useState } from "react"
import {
    Sheet,
    SheetContent,
    SheetDescription,
    SheetHeader,
    SheetTitle,
    SheetTrigger,
} from "@/components/ui/sheet"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { IconPlus } from "@tabler/icons-react"
import { api } from "@/lib/api"
import { toast } from "sonner"

export function TaskCreationSheet({ onTaskCreated }) {
    const [open, setOpen] = useState(false)
    const [loading, setLoading] = useState(false)
    const [formData, setFormData] = useState({
        type: "",
        payload: "",
        priority: 1
    })

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)
        try {
            // Parse payload if it's JSON
            let payloadObj = formData.payload;
            try {
                payloadObj = JSON.parse(formData.payload);
            } catch (e) {
                // Keep as string if not JSON
            }

            await api.tasks.create({
                type: formData.type,
                payload: payloadObj,
                priority: parseInt(formData.priority)
            })
            toast.success("Task created successfully")
            setOpen(false)
            setFormData({ type: "", payload: "", priority: 1 })
            onTaskCreated?.()
        } catch (error) {
            toast.error(error.message || "Failed to create task")
        } finally {
            setLoading(false)
        }
    }

    return (
        <Sheet open={open} onOpenChange={setOpen}>
            <SheetTrigger asChild>
                <Button
                    size="lg"
                    className="gap-2 bg-primary hover:bg-primary/90 text-primary-foreground font-extrabold tracking-tight px-6 h-11 rounded-xl shadow-[0_0_20px_rgba(var(--primary),0.3)] transition-all hover:scale-105"
                >
                    <IconPlus className="size-5 stroke-[3]" />
                    CREATE TASK
                </Button>
            </SheetTrigger>
            <SheetContent className="bg-zinc-950 border-l border-white/5 sm:max-w-md">
                <SheetHeader className="space-y-4">
                    <div className="space-y-1">
                        <span className="text-[10px] font-extrabold uppercase tracking-[0.2em] text-zinc-500">Operation Request</span>
                        <SheetTitle className="text-3xl font-extrabold tracking-tighter text-white">New Task</SheetTitle>
                    </div>
                    <SheetDescription className="text-zinc-500 font-medium leading-relaxed">
                        Configure operation parameters to submit a new execution request to the engine registry.
                    </SheetDescription>
                </SheetHeader>
                <form onSubmit={handleSubmit} className="space-y-6 py-10">
                    <div className="space-y-3">
                        <Label htmlFor="type" className="text-[10px] font-extrabold uppercase tracking-widest text-zinc-500 ml-1">Process Type</Label>
                        <Input
                            id="type"
                            placeholder="e.g. DATA_AGGREGATION"
                            value={formData.type}
                            onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                            className="h-12 bg-zinc-900/50 border-white/5 rounded-lg focus:ring-2 focus:ring-primary/50 transition-all font-bold text-zinc-200"
                            required
                        />
                    </div>
                    <div className="space-y-3">
                        <Label htmlFor="priority" className="text-[10px] font-extrabold uppercase tracking-widest text-zinc-500 ml-1">Scheduling Priority</Label>
                        <Select
                            value={formData.priority.toString()}
                            onValueChange={(val) => setFormData({ ...formData, priority: val })}
                        >
                            <SelectTrigger className="h-12 bg-zinc-900/50 border-white/5 rounded-lg focus:ring-2 focus:ring-primary/50 transition-all font-bold text-zinc-200">
                                <SelectValue placeholder="Select level" />
                            </SelectTrigger>
                            <SelectContent className="bg-zinc-900 border-white/5">
                                <SelectItem value="1" className="font-bold">LOW</SelectItem>
                                <SelectItem value="2" className="font-bold">MEDIUM</SelectItem>
                                <SelectItem value="3" className="font-bold text-blue-400">HIGH</SelectItem>
                                <SelectItem value="4" className="font-bold text-red-400">CRITICAL</SelectItem>
                            </SelectContent>
                        </Select>
                    </div>
                    <div className="space-y-3">
                        <Label htmlFor="payload" className="text-[10px] font-extrabold uppercase tracking-widest text-zinc-500 ml-1">Execution Payload</Label>
                        <textarea
                            id="payload"
                            className="flex min-h-[160px] w-full rounded-lg border border-white/5 bg-zinc-900/50 px-4 py-3 text-sm shadow-sm placeholder:text-zinc-700 focus:outline-none focus:ring-2 focus:ring-primary/50 transition-all font-mono text-zinc-300 scrollbar-thin scrollbar-thumb-zinc-800"
                            placeholder='{"job_id": "99x", "parameters": {...}}'
                            value={formData.payload}
                            onChange={(e) => setFormData({ ...formData, payload: e.target.value })}
                            required
                        />
                    </div>
                    <Button type="submit" className="w-full h-14 rounded-xl bg-primary text-primary-foreground font-extrabold tracking-tight text-lg shadow-[0_0_30px_rgba(var(--primary),0.2)] hover:scale-[1.02] transition-all" disabled={loading}>
                        {loading ? "COMMITTING..." : "SUBMIT OPERATION"}
                    </Button>
                </form>
            </SheetContent>
        </Sheet>
    )
}
