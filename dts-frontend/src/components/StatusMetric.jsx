import { cn } from "@/lib/utils";

/**
 * @param {Object} props
 * @param {LucideIcon} props.icon
 * @param {string} props.label
 * @param {number} props.value - 0 to 100
 * @param {string} [props.className]
 * @param {string} [props.colorClass] - Tailwind text color class
 */
export function StatusMetric({ icon: Icon, label, value, className, colorClass = "text-primary" }) {
  return (
    <div className={cn("flex items-center gap-3 w-full py-1", className)}>
      <Icon className={cn("size-4 shrink-0", colorClass)} />
      <div className="flex-1 space-y-1">
        <div className="flex justify-between items-center text-[10px] uppercase tracking-wider text-muted-foreground font-semibold">
          <span>{label}</span>
          <span>{value}%</span>
        </div>
        <div className="h-1.5 w-full bg-secondary/50 rounded-full overflow-hidden">
          <div
            className={cn("h-full transition-all duration-500 rounded-full", colorClass.replace('text-', 'bg-'))}
            style={{ width: `${value}%` }}
          />
        </div>
      </div>
    </div>
  );
}

/**
 * @param {Object} props
 * @param {LucideIcon} props.icon
 * @param {string} props.value
 * @param {string} [props.className]
 */
export function SimpleMetric({ icon: Icon, value, className }) {
  return (
    <div className={cn("flex items-center gap-2 text-xs text-muted-foreground", className)}>
      <Icon className="size-3" />
      <span>{value}</span>
    </div>
  );
}
