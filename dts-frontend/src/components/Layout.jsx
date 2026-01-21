import React from "react"
import { AppSidebar } from "@/components/app-sidebar"
import { SiteHeader } from "@/components/site-header"
import {
    SidebarInset,
    SidebarProvider,
} from "@/components/ui/sidebar"

export function Layout({ children }) {
    return (
        <SidebarProvider
            style={
                {
                    "--sidebar-width": "calc(var(--spacing) * 60)",
                    "--header-height": "calc(var(--spacing) * 14)",
                }
            }
        >
            <AppSidebar />
            <SidebarInset className="bg-zinc-950">
                <SiteHeader />
                <main className="flex flex-1 flex-col overflow-y-auto">
                    {children}
                </main>
            </SidebarInset>
        </SidebarProvider>
    )
}
