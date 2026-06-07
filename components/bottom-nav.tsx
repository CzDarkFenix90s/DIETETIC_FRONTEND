'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { Home, LayoutGrid, ShoppingCart, Calendar, User } from 'lucide-react'
import { useCart } from '@/lib/cart-context'
import { cn } from '@/lib/utils'

const navItems = [
  { href: '/', label: 'Inicio', icon: Home },
  { href: '/planes', label: 'Planes', icon: LayoutGrid },
  { href: '/carrito', label: 'Carrito', icon: ShoppingCart },
  { href: '/consultas', label: 'Consultas', icon: Calendar },
  { href: '/perfil', label: 'Perfil', icon: User },
]

export function BottomNav() {
  const pathname = usePathname()
  const { itemCount } = useCart()

  return (
    <nav className="fixed bottom-0 left-0 right-0 z-50 mx-auto max-w-md border-t border-border/50 bg-card/95 backdrop-blur-xl supports-[backdrop-filter]:bg-card/80">
      <div className="flex items-center justify-around py-1.5 pb-safe">
        {navItems.map((item) => {
          const isActive = pathname === item.href
          const Icon = item.icon
          const isCart = item.href === '/carrito'

          return (
            <Link
              key={item.href}
              href={item.href}
              className={cn(
                'group relative flex flex-col items-center gap-0.5 px-4 py-2 transition-all duration-200',
                isActive ? 'text-primary' : 'text-muted-foreground hover:text-foreground'
              )}
            >
              <div className={cn(
                'relative flex h-9 w-9 items-center justify-center rounded-2xl transition-all duration-300',
                isActive && 'bg-primary/10 shadow-sm'
              )}>
                <Icon 
                  className={cn(
                    'h-[22px] w-[22px] transition-all duration-200',
                    isActive && 'scale-105'
                  )} 
                  strokeWidth={isActive ? 2.5 : 1.8}
                />
                {isCart && itemCount > 0 && (
                  <span className="absolute -right-0.5 -top-0.5 flex h-4 min-w-4 items-center justify-center rounded-full bg-primary px-1 text-[10px] font-bold text-primary-foreground shadow-sm">
                    {itemCount > 9 ? '9+' : itemCount}
                  </span>
                )}
              </div>
              <span className={cn(
                'text-[10px] font-medium tracking-wide transition-all duration-200',
                isActive ? 'text-primary' : 'text-muted-foreground'
              )}>
                {item.label}
              </span>
            </Link>
          )
        })}
      </div>
    </nav>
  )
}
