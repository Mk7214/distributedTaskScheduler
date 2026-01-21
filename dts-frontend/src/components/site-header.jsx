import { SidebarTrigger } from "@/components/ui/sidebar"
import { Separator } from "@/components/ui/separator"

export function SiteHeader() {
  return (
    <header className="flex h-[var(--header-height)] shrink-0 items-center gap-2 border-b border-white/5 bg-zinc-950/80 backdrop-blur-md sticky top-0 z-50">
      <div className="flex w-full items-center gap-1 px-6">
        <SidebarTrigger className="-ml-1 text-zinc-400 hover:text-primary transition-colors" />
        <Separator orientation="vertical" className="mx-4 h-5 bg-white/10" />
        <div className="flex items-center gap-2">
          <span className="text-[10px] font-extrabold text-zinc-600 uppercase tracking-widest">Environment</span>
          <span className="text-sm font-extrabold text-zinc-200 tracking-tight">Main Cluster</span>
        </div>
        <div className="ml-auto flex items-center gap-4">
          <div className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-green-500/10 border border-green-500/20">
            <span className="size-1.5 rounded-full bg-green-500 animate-pulse" />
            <span className="text-[10px] font-extrabold text-green-500 uppercase tracking-tighter">System Online</span>
          </div>
        </div>
      </div>
    </header>
  );
}
