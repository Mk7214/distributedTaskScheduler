import { NavLink } from "react-router"
import { cn } from "@/lib/utils"
import {
  SidebarGroup,
  SidebarGroupContent,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar"

export function NavMain({ items }) {
  return (
    <SidebarGroup>
      <SidebarGroupContent>
        <SidebarMenu className="gap-2">
          {items.map((item) => (
            <SidebarMenuItem key={item.title}>
              <SidebarMenuButton asChild className="h-11 transition-all duration-200 group">
                <NavLink
                  to={item.url}
                  className={({ isActive }) => cn(
                    "flex items-center w-full gap-3 px-3 py-2 rounded-lg transition-all duration-200",
                    isActive ? "text-primary bg-primary/10 shadow-[inset_0_0_10px_rgba(var(--primary),0.1)]" : "text-zinc-500 hover:text-zinc-300 hover:bg-white/5"
                  )}
                >
                  {item.icon && <item.icon className="size-4 shrink-0 transition-colors" />}
                  <span className="flex-1 font-extrabold tracking-tight text-sm uppercase">{item.title}</span>
                </NavLink>
              </SidebarMenuButton>
            </SidebarMenuItem>
          ))}
        </SidebarMenu>
      </SidebarGroupContent>
    </SidebarGroup>
  );
}
