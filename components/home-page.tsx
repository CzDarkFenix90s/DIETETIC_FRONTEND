'use client'

import Link from 'next/link'
import { ShoppingCart, ChevronRight, Scale, Dumbbell, Leaf, Flame, Trophy, Sparkles, TrendingUp, Clock } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { useCart } from '@/lib/cart-context'
import { planesEjemplo } from '@/lib/mock-data'

const categorias = [
  { id: 'perdida', name: 'Perdida de Peso', icon: Scale, count: 2, gradient: 'from-rose-500 to-orange-400' },
  { id: 'muscular', name: 'Masa Muscular', icon: Dumbbell, count: 2, gradient: 'from-blue-500 to-cyan-400' },
  { id: 'saludable', name: 'Vida Saludable', icon: Leaf, count: 4, gradient: 'from-emerald-500 to-teal-400' },
]

export function HomePage() {
  const { itemCount } = useCart()
  const planesDestacados = planesEjemplo.slice(0, 3)

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="sticky top-0 z-40 bg-card/80 backdrop-blur-xl">
        <div className="flex items-center justify-between px-5 py-4">
          <div className="flex items-center gap-3">
            <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-gradient-to-br from-primary to-primary/80 shadow-lg shadow-primary/25">
              <span className="text-lg font-bold text-primary-foreground">D</span>
            </div>
            <div>
              <h1 className="text-lg font-bold tracking-tight text-foreground">Dietetic</h1>
              <p className="text-[11px] font-medium uppercase tracking-widest text-muted-foreground">Tu bienestar</p>
            </div>
          </div>
          <Link href="/carrito">
            <Button variant="ghost" size="icon" className="relative h-11 w-11 rounded-2xl">
              <ShoppingCart className="h-5 w-5" />
              {itemCount > 0 && (
                <span className="absolute -right-0.5 -top-0.5 flex h-5 w-5 items-center justify-center rounded-full bg-primary text-[10px] font-bold text-primary-foreground shadow-lg">
                  {itemCount}
                </span>
              )}
            </Button>
          </Link>
        </div>
      </header>

      <main className="px-5 pb-8">
        {/* Hero Banner */}
        <section className="mb-8 mt-2">
          <Card className="overflow-hidden border-0 bg-gradient-to-br from-primary via-primary to-primary/90 shadow-xl shadow-primary/20">
            <CardContent className="relative p-6">
              <div className="absolute -right-8 -top-8 h-32 w-32 rounded-full bg-white/10 blur-2xl" />
              <div className="absolute -bottom-4 -left-4 h-24 w-24 rounded-full bg-white/10 blur-xl" />
              <div className="relative">
                <div className="mb-3 inline-flex items-center gap-1.5 rounded-full bg-white/20 px-3 py-1 text-[11px] font-semibold uppercase tracking-wider text-primary-foreground/90">
                  <Sparkles className="h-3 w-3" />
                  Salud sin limites
                </div>
                <h2 className="mb-2 text-2xl font-bold leading-tight tracking-tight text-primary-foreground">
                  Transforma tu vida con nutricion inteligente
                </h2>
                <p className="mb-5 text-sm leading-relaxed text-primary-foreground/80">
                  Planes personalizados creados por expertos para alcanzar tus metas.
                </p>
                <Link href="/planes">
                  <Button 
                    variant="secondary" 
                    className="h-11 gap-2 rounded-xl bg-white px-5 font-semibold text-primary shadow-lg hover:bg-white/90"
                  >
                    Explorar planes
                    <ChevronRight className="h-4 w-4" />
                  </Button>
                </Link>
              </div>
            </CardContent>
          </Card>
        </section>

        {/* Stats Row */}
        <section className="mb-8">
          <div className="grid grid-cols-3 gap-3">
            <Card className="border-0 bg-gradient-to-br from-amber-50 to-orange-50 shadow-sm">
              <CardContent className="flex flex-col items-center p-4">
                <div className="mb-1.5 flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-amber-400 to-orange-500 shadow-lg shadow-orange-200">
                  <Flame className="h-5 w-5 text-white" />
                </div>
                <span className="text-lg font-bold text-foreground">0</span>
                <span className="text-[10px] font-medium text-muted-foreground">Dias racha</span>
              </CardContent>
            </Card>
            <Card className="border-0 bg-gradient-to-br from-emerald-50 to-teal-50 shadow-sm">
              <CardContent className="flex flex-col items-center p-4">
                <div className="mb-1.5 flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-emerald-400 to-teal-500 shadow-lg shadow-emerald-200">
                  <TrendingUp className="h-5 w-5 text-white" />
                </div>
                <span className="text-lg font-bold text-foreground">0</span>
                <span className="text-[10px] font-medium text-muted-foreground">XP Total</span>
              </CardContent>
            </Card>
            <Card className="border-0 bg-gradient-to-br from-violet-50 to-purple-50 shadow-sm">
              <CardContent className="flex flex-col items-center p-4">
                <div className="mb-1.5 flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-violet-400 to-purple-500 shadow-lg shadow-violet-200">
                  <Trophy className="h-5 w-5 text-white" />
                </div>
                <span className="text-lg font-bold text-foreground">0</span>
                <span className="text-[10px] font-medium text-muted-foreground">Logros</span>
              </CardContent>
            </Card>
          </div>
        </section>

        {/* Categorias */}
        <section className="mb-8">
          <div className="mb-4 flex items-center justify-between">
            <div>
              <h3 className="text-lg font-bold text-foreground">Categorias</h3>
              <p className="text-sm text-muted-foreground">Encuentra tu plan ideal</p>
            </div>
            <Link href="/planes" className="flex items-center gap-1 text-sm font-semibold text-primary">
              Ver todo
              <ChevronRight className="h-4 w-4" />
            </Link>
          </div>
          <div className="grid grid-cols-3 gap-3">
            {categorias.map((cat) => {
              const Icon = cat.icon
              return (
                <Link key={cat.id} href="/planes">
                  <Card className="group border-0 shadow-sm transition-all duration-300 hover:shadow-md hover:-translate-y-0.5">
                    <CardContent className="flex flex-col items-center p-4">
                      <div className={`mb-2.5 flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-to-br ${cat.gradient} shadow-lg transition-transform duration-300 group-hover:scale-105`}>
                        <Icon className="h-6 w-6 text-white" />
                      </div>
                      <p className="text-center text-xs font-semibold text-foreground">
                        {cat.name}
                      </p>
                      <p className="text-[10px] text-muted-foreground">{cat.count} planes</p>
                    </CardContent>
                  </Card>
                </Link>
              )
            })}
          </div>
        </section>

        {/* Planes Destacados */}
        <section>
          <div className="mb-4 flex items-center justify-between">
            <div>
              <h3 className="text-lg font-bold text-foreground">Planes destacados</h3>
              <p className="text-sm text-muted-foreground">Los mas populares</p>
            </div>
            <Link href="/planes" className="flex items-center gap-1 text-sm font-semibold text-primary">
              Ver todo
              <ChevronRight className="h-4 w-4" />
            </Link>
          </div>
          <div className="space-y-3">
            {planesDestacados.map((plan, index) => (
              <Link key={plan.id} href="/planes">
                <Card className="group border-0 shadow-sm transition-all duration-300 hover:shadow-md">
                  <CardContent className="flex items-center gap-4 p-4">
                    <div className={`flex h-14 w-14 flex-shrink-0 items-center justify-center rounded-2xl bg-gradient-to-br ${
                      index === 0 ? 'from-rose-500 to-orange-400' : 
                      index === 1 ? 'from-blue-500 to-cyan-400' : 
                      'from-emerald-500 to-teal-400'
                    } shadow-lg`}>
                      {index === 0 ? <Scale className="h-6 w-6 text-white" /> :
                       index === 1 ? <Dumbbell className="h-6 w-6 text-white" /> :
                       <Leaf className="h-6 w-6 text-white" />}
                    </div>
                    <div className="flex-1 min-w-0">
                      <h4 className="font-semibold text-foreground truncate">{plan.name}</h4>
                      <p className="text-sm text-muted-foreground line-clamp-1">{plan.description}</p>
                      <div className="mt-1 flex items-center gap-2">
                        <span className="flex items-center gap-1 text-xs text-muted-foreground">
                          <Clock className="h-3 w-3" />
                          {plan.duration_weeks} semanas
                        </span>
                        <span className="text-xs text-muted-foreground">·</span>
                        <span className="text-xs text-muted-foreground">{plan.target_calories} kcal</span>
                      </div>
                    </div>
                    <div className="flex flex-col items-end">
                      <span className="text-lg font-bold text-primary">${plan.estimated_cost}</span>
                      <ChevronRight className="h-4 w-4 text-muted-foreground transition-transform group-hover:translate-x-0.5" />
                    </div>
                  </CardContent>
                </Card>
              </Link>
            ))}
          </div>
        </section>
      </main>
    </div>
  )
}
