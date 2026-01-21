import React from "react"

import {
    Sheet,
    SheetContent,
    SheetDescription,
    SheetHeader,
    SheetTitle,
} from "@/components/ui/sheet"

export function LogViewer({ open, onOpenChange, logs, title }) {
    return (
        <Sheet open={open} onOpenChange={onOpenChange}>
            <SheetContent side="right" className="sm:max-w-2xl">
                <SheetHeader>
                    <SheetTitle>{title}</SheetTitle>
                    <SheetDescription>
                        Live application logs and process output.
                    </SheetDescription>
                </SheetHeader>
                <div className="mt-6 h-[calc(100vh-120px)] w-full rounded-md bg-zinc-950 p-4 font-mono text-xs text-zinc-50 overflow-auto border border-zinc-800 shadow-2xl">
                    {logs && logs.length > 0 ? (
                        <div className="space-y-1">
                            {logs.map((log, i) => (
                                <div key={i} className="whitespace-pre-wrap py-0.5 border-b border-zinc-900/50">
                                    <span className="text-zinc-500 mr-2">[{i + 1}]</span>
                                    {log}
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="flex h-full items-center justify-center text-zinc-500 italic">
                            No logs available at this time.
                        </div>
                    )}
                </div>
            </SheetContent>
        </Sheet>
    )
}
