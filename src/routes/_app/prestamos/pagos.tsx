import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_app/prestamos/pagos')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Pagos</div>
}
