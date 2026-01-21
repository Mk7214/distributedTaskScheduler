import * as React from "react"
import {
  IconDashboard,
  IconInnerShadowTop,
} from "@tabler/icons-react"

import { NavMain } from "@/components/nav-main"
import {
  Sidebar,
  SidebarContent,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar"

const data = {
  navMain: [
    {
      title: "Dashboard",
      url: "/dashboard",
      icon: IconDashboard,
    },
    {
      title: "Schedulers",
      url: "/schedulers",
      icon: IconInnerShadowTop,
    },
    {
      title: "Workers",
      url: "/workers",
      icon: IconDashboard,
    },
  ]
}

export function AppSidebar({
  ...props
}) {
  return (
    <Sidebar side="left" collapsible="offcanvas" {...props}>
      <SidebarHeader>
        <SidebarMenu>
          <SidebarMenuItem>
            <SidebarMenuButton asChild className="data-[slot=sidebar-menu-button]:!p-1.5 focus:bg-transparent hover:bg-transparent">
              <a href="#">
                <IconInnerShadowTop className="!size-5 text-primary" />
                <span className="text-base font-extrabold tracking-tighter uppercase text-white">DTS Engine</span>
              </a>
            </SidebarMenuButton>
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarHeader>
      <SidebarContent>
        <NavMain items={data.navMain} />
      </SidebarContent>
    </Sidebar>
  );
}
