'use client'

import { useState } from 'react'
import { Search, Scale, Dumbbell, Leaf, Activity, Sparkles, Clock, Flame, Plus, Check } from 'lucide-react'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { useCart } from '@/lib/cart-context'
import { planesEjemplo } from '@/lib/mock-data'
import { useToast } from '@/hooks/use-toast'
import { cn } from '@/lib/utils'
import type { PlanNutricional } from '@/lib/types'

const categorias = [
  { id: 'todos', name: 'Todos', icon: Sparkles, gradient: 'from-slate-500 to-slate-600' },
  { id: 'perdida', name: 'Perdida', icon: Scale, goal: 'Perdida de peso', gradient: 'from-rose-500 to-orange-400' },
  { id: 'muscular', name: 'Muscular', icon: Dumbbell, goal: 'Ganancia muscular', gradient: 'from-blue-500 to-cyan-400' },
  { id: 'mantenimiento', name: 'Saludable', icon: Leaf, goal: 'Mantenimiento', gradient: 'from-emerald-500 to-teal-400' },
  { id: 'rendimiento', name: 'Deportivo', icon: Activity, goal: 'Rendimiento', gradient: 'from-violet-500 to-purple-400' },
]

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

export function PlanesPage() {
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedCategory, setSelectedCategory] = useState('todos')
  const { addToCart, items } = useCart()
  const { toast } = useToast()

  const filteredPlanes = planesEjemplo.filter(plan => {
    const matchesSearch = plan.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                          plan.description.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesCategory = selectedCategory === 'todos' || 
                           plan.goal === categorias.find(c => c.id === selectedCategory)?.goal
    return matchesSearch && matchesCategory
  })

  const isInCart = (planId: number) => items.some(item => item.plan.id === planId)

  const handleAddToCart = (plan: PlanNutricional) => {
    addToCart(plan)
    toast({
      title: 'Agregado al carrito',
      description: `${plan.name} ha sido agregado.`,
    })
  }

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="sticky top-0 z-40 bg-card/80 backdrop-blur-xl">
        <div className="px-5 py-4">
          <h1 className="mb-1 text-2xl font-bold tracking-tight text-foreground">Planes</h1>
          <p className="mb-4 text-sm text-muted-foreground">Encuentra el plan perfecto para ti</p>
          
          {/* Search */}
          <div className="relative">
            <Search className="absolute left-4 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <Input
              placeholder="Buscar planes..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="h-12 rounded-2xl border-0 bg-secondary pl-11 text-sm shadow-sm placeholder:text-muted-foreground focus-visible:ring-2 focus-visible:ring-primary/20"
            />
          </div>
        </div>

        {/* Category Pills */}
        <div className="flex gap-2 overflow-x-auto px-5 pb-4 scrollbar-hide">
          {categorias.map((cat) => {
            const isSelected = selectedCategory === cat.id
            const Icon = cat.icon
            return (
              <button
                key={cat.id}
                onClick={() => setSelectedCategory(cat.id)}
                className={cn(
                  'flex flex-shrink-0 items-center gap-1.5 rounded-full px-4 py-2 text-sm font-medium transition-all duration-200',
                  isSelected 
                    ? 'bg-primary text-primary-foreground shadow-lg shadow-primary/25' 
                    : 'bg-secondary text-secondary-foreground hover:bg-secondary/80'
                )}
              >
                <Icon className="h-4 w-4" />
                {cat.name}
              </button>
            )
          })}
        </div>
      </header>

      <main className="px-5 pb-8">
        {/* Results Count */}
        <div className="mb-4 flex items-center justify-between py-2">
          <p className="text-sm font-medium text-muted-foreground">
            {filteredPlanes.length} planes disponibles
          </p>
        </div>

        {/* Plans Grid */}
        {filteredPlanes.length > 0 ? (
          <div className="space-y-4">
            {filteredPlanes.map((plan) => {
              const Icon = getIconForPlan(plan.goal)
              const gradient = getGradientForPlan(plan.goal)
              const inCart = isInCart(plan.id)
              
              return (
                <Card key={plan.id} className="group overflow-hidden border-0 shadow-sm transition-all duration-300 hover:shadow-md">
                  <CardContent className="p-0">
                    <div className="flex gap-4 p-4">
                      {/* Icon */}
                      <div className={`flex h-16 w-16 flex-shrink-0 items-center justify-center rounded-2xl bg-gradient-to-br ${gradient} shadow-lg`}>
                        <Icon className="h-7 w-7 text-white" />
                      </div>
                      
                      {/* Content */}
                      <div className="flex-1 min-w-0">
                        <div className="mb-1 flex items-start justify-between gap-2">
                          <h3 className="font-semibold text-foreground leading-tight">{plan.name}</h3>
                          <span className="flex-shrink-0 rounded-lg bg-primary/10 px-2 py-0.5 text-sm font-bold text-primary">
                            ${plan.estimated_cost}
                          </span>
                        </div>
                        <p className="mb-3 text-sm text-muted-foreground line-clamp-2">{plan.description}</p>
                        
                        {/* Meta */}
                        <div className="mb-3 flex items-center gap-3">
                          <span className="flex items-center gap-1 text-xs text-muted-foreground">
                            <Clock className="h-3.5 w-3.5" />
                            {plan.duration_weeks} semanas
                          </span>
                          <span className="flex items-center gap-1 text-xs text-muted-foreground">
                            <Flame className="h-3.5 w-3.5" />
                            {plan.target_calories} kcal
                          </span>
                        </div>

                        {/* CTA */}
                        <Button
                          onClick={() => handleAddToCart(plan)}
                          disabled={inCart}
                          size="sm"
                          className={cn(
                            'h-9 w-full rounded-xl font-semibold transition-all duration-200',
                            inCart 
                              ? 'bg-emerald-500 hover:bg-emerald-500 text-white' 
                              : 'bg-primary hover:bg-primary/90'
                          )}
                        >
                          {inCart ? (
                            <>
                              <Check className="mr-1.5 h-4 w-4" />
                              En el carrito
                            </>
                          ) : (
                            <>
                              <Plus className="mr-1.5 h-4 w-4" />
                              Agregar al carrito
                            </>
                          )}
                        </Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              )
            })}
          </div>
        ) : (
          <div className="flex flex-col items-center justify-center py-16">
            <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-full bg-secondary">
              <Search className="h-7 w-7 text-muted-foreground" />
            </div>
            <h3 className="mb-1 font-semibold text-foreground">No se encontraron planes</h3>
            <p className="text-center text-sm text-muted-foreground">
              Intenta con otra busqueda o categoria
            </p>
          </div>
        )}
      </main>
    </div>
  )
}
