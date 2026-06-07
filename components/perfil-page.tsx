'use client'

import { Settings, LogOut, Zap, Flame, FileText, Star, Award, ChevronRight, Crown, Trophy, Target, Medal } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { useToast } from '@/hooks/use-toast'
import { cn } from '@/lib/utils'

const perfilUsuario = {
  name: 'Usuario',
  email: 'usuario@gmail.com',
  level: 'Nivel Intermedio',
  status: 'Activo',
  xp: 1250,
  diasRacha: 7,
  planesActivos: 2,
  logros: [
    { id: 1, name: 'Inalcanzable', description: '7 dias de racha', unlocked: true, icon: Flame, gradient: 'from-orange-400 to-rose-500' },
    { id: 2, name: 'Erudito', description: 'Consigue 500 XP', unlocked: true, icon: Star, gradient: 'from-amber-400 to-yellow-500' },
    { id: 3, name: 'Constante', description: '30 dias de racha', unlocked: false, icon: Target, gradient: 'from-slate-300 to-slate-400' },
    { id: 4, name: 'Maestro', description: 'Completa 5 planes', unlocked: false, icon: Medal, gradient: 'from-slate-300 to-slate-400' },
  ]
}

export function PerfilPage() {
  const { toast } = useToast()

  const handleLogout = () => {
    toast({
      title: 'Cerrando sesion...',
      description: 'Hasta pronto!',
    })
  }

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="sticky top-0 z-40 bg-card/80 backdrop-blur-xl">
        <div className="flex items-center justify-between px-5 py-4">
          <div>
            <h1 className="text-2xl font-bold tracking-tight text-foreground">Mi Perfil</h1>
            <p className="text-sm text-muted-foreground">Tu progreso personal</p>
          </div>
          <Button variant="ghost" size="icon" className="h-10 w-10 rounded-xl">
            <Settings className="h-5 w-5" />
          </Button>
        </div>
      </header>

      <main className="px-5 pb-8">
        {/* Profile Card */}
        <Card className="mb-6 overflow-hidden border-0 shadow-sm">
          <div className="h-20 bg-gradient-to-br from-primary via-primary/90 to-primary/80" />
          <CardContent className="relative px-5 pb-6">
            <Avatar className="-mt-12 mb-4 h-24 w-24 border-4 border-card bg-primary shadow-xl">
              <AvatarFallback className="bg-gradient-to-br from-primary to-primary/80 text-3xl font-bold text-primary-foreground">
                {perfilUsuario.name.charAt(0).toUpperCase()}
              </AvatarFallback>
            </Avatar>
            <h2 className="text-xl font-bold text-foreground">{perfilUsuario.name}</h2>
            <p className="mb-3 text-sm text-muted-foreground">{perfilUsuario.email}</p>
            <span className="inline-flex items-center gap-1.5 rounded-full bg-primary/10 px-3 py-1 text-xs font-semibold text-primary">
              <Trophy className="h-3.5 w-3.5" />
              {perfilUsuario.level} - {perfilUsuario.status}
            </span>
          </CardContent>
        </Card>

        {/* Stats */}
        <div className="mb-6 grid grid-cols-3 gap-3">
          <Card className="border-0 bg-gradient-to-br from-blue-50 to-indigo-50 shadow-sm">
            <CardContent className="flex flex-col items-center p-4">
              <div className="mb-2 flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-blue-500 to-indigo-500 shadow-lg shadow-blue-200">
                <Zap className="h-5 w-5 text-white" />
              </div>
              <span className="text-lg font-bold text-foreground">{perfilUsuario.xp.toLocaleString()}</span>
              <span className="text-[10px] font-medium text-muted-foreground">XP Total</span>
            </CardContent>
          </Card>
          <Card className="border-0 bg-gradient-to-br from-orange-50 to-rose-50 shadow-sm">
            <CardContent className="flex flex-col items-center p-4">
              <div className="mb-2 flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-orange-400 to-rose-500 shadow-lg shadow-orange-200">
                <Flame className="h-5 w-5 text-white" />
              </div>
              <span className="text-lg font-bold text-foreground">{perfilUsuario.diasRacha}</span>
              <span className="text-[10px] font-medium text-muted-foreground">Dias racha</span>
            </CardContent>
          </Card>
          <Card className="border-0 bg-gradient-to-br from-emerald-50 to-teal-50 shadow-sm">
            <CardContent className="flex flex-col items-center p-4">
              <div className="mb-2 flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-emerald-400 to-teal-500 shadow-lg shadow-emerald-200">
                <FileText className="h-5 w-5 text-white" />
              </div>
              <span className="text-lg font-bold text-foreground">{perfilUsuario.planesActivos}</span>
              <span className="text-[10px] font-medium text-muted-foreground">Planes</span>
            </CardContent>
          </Card>
        </div>

        {/* Premium Banner */}
        <Card className="mb-6 overflow-hidden border-0 bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 shadow-xl">
          <CardContent className="flex items-center justify-between p-5">
            <div className="flex items-center gap-4">
              <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-to-br from-amber-400 to-yellow-500 shadow-lg">
                <Crown className="h-6 w-6 text-white" />
              </div>
              <div>
                <h3 className="font-bold text-white">Dietetic Premium</h3>
                <p className="text-sm text-slate-400">Acceso ilimitado a todo</p>
              </div>
            </div>
            <Button 
              size="sm" 
              className="h-9 rounded-xl bg-white font-semibold text-slate-900 hover:bg-slate-100"
            >
              Ver
              <ChevronRight className="ml-0.5 h-4 w-4" />
            </Button>
          </CardContent>
        </Card>

        {/* Achievements */}
        <section className="mb-6">
          <div className="mb-4 flex items-center justify-between">
            <div>
              <h3 className="text-lg font-bold text-foreground">Mis logros</h3>
              <p className="text-sm text-muted-foreground">
                {perfilUsuario.logros.filter(l => l.unlocked).length} de {perfilUsuario.logros.length} desbloqueados
              </p>
            </div>
            <Button variant="ghost" size="sm" className="text-primary">
              Ver todos
              <ChevronRight className="ml-0.5 h-4 w-4" />
            </Button>
          </div>
          <div className="grid grid-cols-2 gap-3">
            {perfilUsuario.logros.map((logro) => {
              const Icon = logro.icon
              return (
                <Card 
                  key={logro.id} 
                  className={cn(
                    'overflow-hidden border-0 shadow-sm transition-all duration-300',
                    !logro.unlocked && 'opacity-60'
                  )}
                >
                  <CardContent className="flex flex-col items-center p-4">
                    <div className={cn(
                      'mb-3 flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br shadow-lg',
                      logro.gradient,
                      logro.unlocked ? 'shadow-amber-200' : 'shadow-slate-200'
                    )}>
                      <Icon className="h-7 w-7 text-white" />
                    </div>
                    <h4 className="text-center text-sm font-semibold text-foreground">
                      {logro.name}
                    </h4>
                    <p className="text-center text-[10px] text-muted-foreground">
                      {logro.description}
                    </p>
                    {logro.unlocked && (
                      <span className="mt-2 flex items-center gap-1 text-[10px] font-semibold text-emerald-600">
                        <Award className="h-3 w-3" />
                        Desbloqueado
                      </span>
                    )}
                  </CardContent>
                </Card>
              )
            })}
          </div>
        </section>

        {/* Logout Button */}
        <Button 
          variant="outline" 
          className="h-12 w-full rounded-xl border-destructive/30 font-semibold text-destructive hover:bg-destructive/10"
          onClick={handleLogout}
        >
          <LogOut className="mr-2 h-4 w-4" />
          Cerrar sesion
        </Button>
      </main>
    </div>
  )
}
