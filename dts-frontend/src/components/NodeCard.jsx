import React from "react"
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
    Play,
    Square,
    Terminal,
    Activity,
    Cpu,
    Database,
} from "lucide-react"
import { cn } from "@/lib/utils"

export function NodeCard({
    id,
    name,
    status,
    description,
    type,
    onStart,
    onStop,
    onLogs,
}) {
    const isRunning = status.toLowerCase() === "running"
    const isStopped = status.toLowerCase() === "stopped"
    const isFailed = status.toLowerCase() === "failed"

    return (
        <Card className="overflow-hidden border-none bg-zinc-900/40 hover:bg-zinc-900/60 transition-all duration-300 shadow-xl group">
            <CardHeader className="pb-3 border-b border-white/5">
                <div className="flex items-center justify-between mb-2">
                    <Badge
                        variant="outline"
                        className={cn(
                            "font-bold uppercase tracking-widest text-[10px] py-0.5 px-2",
                            type === "scheduler" ? "text-blue-400 border-blue-400/20 bg-blue-400/5" : "text-purple-400 border-purple-400/20 bg-purple-400/5"
                        )}
                    >
                        {type}
                    </Badge>
                    <div className="flex items-center gap-1.5">
                        <span className={cn(
                            "size-2 rounded-full animate-pulse",
                            isRunning ? "bg-green-500 shadow-[0_0_8px_rgba(34,197,94,0.5)]" :
                                isStopped ? "bg-zinc-600" : "bg-red-500"
                        )} />
                        <span className={cn(
                            "text-[10px] font-extrabold uppercase tracking-tighter",
                            isRunning ? "text-green-500" : isStopped ? "text-zinc-500" : "text-red-500"
                        )}>
                            {status}
                        </span>
                    </div>
                </div>
                <CardTitle className="text-xl font-extrabold tracking-tight text-zinc-100 group-hover:text-primary transition-colors">
                    {name}
                </CardTitle>
                <CardDescription className="text-xs text-zinc-500 font-medium line-clamp-1">
                    {description || `Active ${type} node management.`}
                </CardDescription>
            </CardHeader>

            <CardContent className="py-6">
                <div className="grid grid-cols-2 gap-4">
                    <div className="space-y-1">
                        <p className="text-[10px] font-bold text-zinc-600 uppercase tracking-widest">ID Hash</p>
                        <p className="text-xs font-mono font-extrabold text-zinc-300 truncate">{id}</p>
                    </div>
                    <div className="space-y-1 text-right">
                        <p className="text-[10px] font-bold text-zinc-600 uppercase tracking-widest">Type</p>
                        <p className="text-xs font-extrabold text-zinc-300 capitalize">{type}</p>
                    </div>
                </div>

                <div className="mt-6 flex items-center justify-between p-3 rounded-lg bg-zinc-950/40 border border-white/5">
                    <div className="flex items-center gap-3">
                        <div className="p-2 rounded-md bg-zinc-900 shadow-inner">
                            {type === "scheduler" ? <Cpu className="size-4 text-zinc-500" /> : <Database className="size-4 text-zinc-500" />}
                        </div>
                        <div>
                            <p className="text-[10px] font-bold text-zinc-600 uppercase">Load Status</p>
                            <p className="text-sm font-extrabold text-white">NOMINAL</p>
                        </div>
                    </div>
                    <Activity className={cn("size-4", isRunning ? "text-green-500/50" : "text-zinc-700")} />
                </div>
            </CardContent>

            <CardFooter className="gap-2 bg-zinc-950/20 border-t border-white/5 pt-4 pb-4">
                <Button
                    variant="outline"
                    size="sm"
                    className={cn(
                        "flex-1 font-extrabold tracking-tight border-zinc-800 transition-all",
                        !isRunning && "hover:bg-green-500/10 hover:text-green-500 hover:border-green-500/20"
                    )}
                    onClick={onStart}
                    disabled={isRunning}
                >
                    <Play className="mr-2 size-3 fill-current" />
                    START
                </Button>
                <Button
                    variant="outline"
                    size="sm"
                    className={cn(
                        "flex-1 font-extrabold tracking-tight border-zinc-800 transition-all",
                        isRunning && "hover:bg-red-500/10 hover:text-red-500 hover:border-red-500/20"
                    )}
                    onClick={() => onStop(id)}
                    disabled={!isRunning}
                >
                    <Square className="mr-2 size-3 fill-current" />
                    STOP
                </Button>
                <Button
                    variant="secondary"
                    size="sm"
                    className="px-3 group-hover:bg-primary group-hover:text-primary-foreground transition-all duration-300"
                    onClick={onLogs}
                >
                    <Terminal className="size-3" />
                </Button>
            </CardFooter>
        </Card>
    )
}
