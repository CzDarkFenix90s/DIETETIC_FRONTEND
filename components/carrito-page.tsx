'use client'

import Link from 'next/link'
import { ShoppingCart, Trash2, Scale, Dumbbell, Leaf, Activity, ChevronRight, ShieldCheck, CreditCard, Clock } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Separator } from '@/components/ui/separator'
import { useCart } from '@/lib/cart-context'
import { useToast } from '@/hooks/use-toast'

const getIconForPlan = (goal: string) => {
  switch (goal) {
    case 'Perdida de peso': return Scale
    case 'Ganancia muscular': return Dumbbell
    case 'Rendimiento': return Activity
    default: return Leaf
  }
}

const getGradientForPlan = (goal: string) => {
  switch (goal) {
    case 'Perdida de peso': return 'from-rose-500 to-orange-400'
    case 'Ganancia muscular': return 'from-blue-500 to-cyan-400'
    case 'Rendimiento': return 'from-violet-500 to-purple-400'
    default: return 'from-emerald-500 to-teal-400'
  }
}

export function CarritoPage() {
  const { items, removeFromCart, getTotal, clearCart } = useCart()
  const { toast } = useToast()

  const handleCheckout = () => {
    if (items.length === 0) {
      toast({
        title: 'Carrito vacio',
        description: 'Agrega planes antes de continuar.',
        variant: 'destructive',
      })
      return
    }
    toast({
      title: 'Procesando...',
      description: 'Redirigiendo a checkout...',
    })
  }

  if (items.length === 0) {
    return (
      <div className="flex min-h-screen flex-col bg-background">
        <header className="sticky top-0 z-40 bg-card/80 backdrop-blur-xl">
          <div className="px-5 py-4">
            <h1 className="text-2xl font-bold tracking-tight text-foreground">Mi Carrito</h1>
            <p className="text-sm text-muted-foreground">Tus planes seleccionados</p>
          </div>
        </header>
        
        <main className="flex flex-1 flex-col items-center justify-center px-5">
          <div className="mb-5 flex h-24 w-24 items-center justify-center rounded-full bg-secondary">
            <ShoppingCart className="h-10 w-10 text-muted-foreground" />
          </div>
          <h2 className="mb-2 text-xl font-bold text-foreground">Tu carrito esta vacio</h2>
          <p className="mb-8 text-center text-sm text-muted-foreground max-w-xs">
            Explora nuestros planes nutricionales y comienza tu transformacion hoy.
          </p>
          <Link href="/planes" className="w-full max-w-xs">
            <Button className="h-12 w-full rounded-2xl font-semibold shadow-lg shadow-primary/25">
              Explorar Planes
              <ChevronRight className="ml-1 h-4 w-4" />
            </Button>
          </Link>
        </main>
      </div>
    )
  }

  return (
    <div className="flex min-h-screen flex-col bg-background">
      <header className="sticky top-0 z-40 bg-card/80 backdrop-blur-xl">
        <div className="flex items-center justify-between px-5 py-4">
          <div>
            <h1 className="text-2xl font-bold tracking-tight text-foreground">Mi Carrito</h1>
            <p className="text-sm text-muted-foreground">{items.length} {items.length === 1 ? 'plan' : 'planes'}</p>
          </div>
          <Button 
            variant="ghost" 
            size="sm" 
            onClick={clearCart}
            className="text-destructive hover:text-destructive hover:bg-destructive/10"
          >
            Vaciar todo
          </Button>
        </div>
      </header>

      <main className="flex-1 px-5 pb-48">
        <div className="space-y-3 py-4">
          {items.map((item) => {
            const Icon = getIconForPlan(item.plan.goal)
            const gradient = getGradientForPlan(item.plan.goal)
            
            return (
              <Card key={item.plan.id} className="overflow-hidden border-0 shadow-sm">
                <CardContent className="p-4">
                  <div className="flex gap-4">
                    <div className={`flex h-14 w-14 flex-shrink-0 items-center justify-center rounded-2xl bg-gradient-to-br ${gradient} shadow-lg`}>
                      <Icon className="h-6 w-6 text-white" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2">
                        <div>
                          <h3 className="font-semibold text-foreground">{item.plan.name}</h3>
                          <div className="mt-0.5 flex items-center gap-1 text-xs text-muted-foreground">
                            <Clock className="h-3 w-3" />
                            {item.plan.duration_weeks} semanas
                          </div>
                        </div>
                        <button 
                          className="flex h-8 w-8 items-center justify-center rounded-xl text-muted-foreground transition-colors hover:bg-destructive/10 hover:text-destructive"
                          onClick={() => removeFromCart(item.plan.id)}
                        >
                          <Trash2 className="h-4 w-4" />
                        </button>
                      </div>
                      <div className="mt-2 flex items-center justify-between">
                        <span className="text-lg font-bold text-primary">
                          ${(item.plan.estimated_cost * item.quantity).toFixed(2)}
                        </span>
                        <span className="rounded-lg bg-secondary px-2 py-0.5 text-xs font-medium text-secondary-foreground">
                          Qty: {item.quantity}
                        </span>
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            )
          })}
        </div>

        {/* Summary */}
        <Card className="mt-4 border-0 shadow-sm">
          <CardContent className="p-5">
            <h3 className="mb-4 font-bold text-foreground">Resumen del pedido</h3>
            
            <div className="space-y-3 text-sm">
              <div className="flex justify-between">
                <span className="text-muted-foreground">Subtotal ({items.length} {items.length === 1 ? 'plan' : 'planes'})</span>
                <span className="font-medium text-foreground">${getTotal().toFixed(2)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Descuento</span>
                <span className="font-medium text-emerald-500">-$0.00</span>
              </div>
            </div>
            
            <Separator className="my-4" />
            
            <div className="flex justify-between">
              <span className="text-lg font-bold text-foreground">Total</span>
              <span className="text-xl font-bold text-primary">${getTotal().toFixed(2)}</span>
            </div>

            {/* Trust badges */}
            <div className="mt-5 flex items-center justify-center gap-4 text-xs text-muted-foreground">
              <div className="flex items-center gap-1">
                <ShieldCheck className="h-3.5 w-3.5" />
                Pago seguro
              </div>
              <div className="flex items-center gap-1">
                <CreditCard className="h-3.5 w-3.5" />
                SSL encriptado
              </div>
            </div>
          </CardContent>
        </Card>
      </main>

      {/* Checkout Button */}
      <div className="fixed bottom-16 left-0 right-0 z-40 mx-auto max-w-md border-t border-border/50 bg-card/95 p-4 backdrop-blur-xl">
        <Button 
          className="h-14 w-full rounded-2xl text-base font-semibold shadow-lg shadow-primary/25" 
          onClick={handleCheckout}
        >
          Proceder al pago - ${getTotal().toFixed(2)}
        </Button>
      </div>
    </div>
  )
}
