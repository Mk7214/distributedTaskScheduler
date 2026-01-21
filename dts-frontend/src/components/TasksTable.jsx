import React from "react"
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { cn } from "@/lib/utils"

export function TasksTable({ tasks, title }) {
    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between px-1">
                <h3 className="text-xl font-extrabold tracking-tight text-zinc-200">{title}</h3>
                <Badge variant="secondary" className="font-extrabold text-[10px] uppercase tracking-widest bg-zinc-800 text-zinc-400">
                    {tasks.length} {tasks.length === 1 ? 'Record' : 'Records'}
                </Badge>
            </div>
            <div className="rounded-xl border border-white/5 bg-zinc-950/50 backdrop-blur-sm overflow-hidden shadow-inner">
                <Table>
                    <TableHeader className="bg-zinc-900/50">
                        <TableRow className="border-white/5 hover:bg-transparent">
                            <TableHead className="w-[120px] font-extrabold uppercase tracking-widest text-[10px] text-zinc-500">Task-ID</TableHead>
                            <TableHead className="font-extrabold uppercase tracking-widest text-[10px] text-zinc-500">Operation</TableHead>
                            <TableHead className="font-extrabold uppercase tracking-widest text-[10px] text-zinc-500">Priority</TableHead>
                            <TableHead className="font-extrabold uppercase tracking-widest text-[10px] text-zinc-500">Payload</TableHead>
                            <TableHead className="text-right font-extrabold uppercase tracking-widest text-[10px] text-zinc-500 px-6">Outcome</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {tasks.length === 0 ? (
                            <TableRow className="border-none">
                                <TableCell colSpan={5} className="h-40 text-center text-zinc-600 font-bold uppercase tracking-widest text-xs italic">
                                    No operations archived in registry.
                                </TableCell>
                            </TableRow>
                        ) : (
                            tasks.map((task) => (
                                <TableRow key={task.id} className="border-white/5 hover:bg-white/[0.02] transition-colors group">
                                    <TableCell className="font-mono text-xs font-extrabold text-zinc-400 group-hover:text-primary transition-colors">{task.id}</TableCell>
                                    <TableCell>
                                        <Badge variant="outline" className="font-extrabold tracking-tight bg-zinc-950/50 border-white/5 text-zinc-400 group-hover:border-primary/30 transition-colors uppercase text-[10px]">
                                            {task.taskType || task.type}
                                        </Badge>
                                    </TableCell>
                                    <TableCell>
                                        <Badge
                                            className={cn(
                                                "font-extrabold text-[10px] shadow-none",
                                                task.priority > 2 ? "bg-red-500/10 text-red-400 border border-red-500/20" : "bg-blue-500/10 text-blue-400 border border-blue-500/20"
                                            )}
                                        >
                                            {task.priority || 1}
                                        </Badge>
                                    </TableCell>
                                    <TableCell className="max-w-[200px] truncate text-xs font-medium text-zinc-500 group-hover:text-zinc-300 transition-colors">
                                        {typeof task.payload === 'string' ? task.payload : JSON.stringify(task.payload)}
                                    </TableCell>
                                    <TableCell className="text-right px-6">
                                        <div className="flex items-center justify-end gap-2">
                                            <span className={cn(
                                                "size-1.5 rounded-full",
                                                title.toLowerCase().includes("dead") ? "bg-red-500" : "bg-green-500"
                                            )} />
                                            <span className={cn(
                                                "font-extrabold text-[10px] tracking-tight",
                                                title.toLowerCase().includes("dead") ? "text-red-500" : "text-green-500"
                                            )}>
                                                {title.toLowerCase().includes("dead") ? "TERMINATED" : "SUCCESS"}
                                            </span>
                                        </div>
                                    </TableCell>
                                </TableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </div>
        </div>
    )
}
