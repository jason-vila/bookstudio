import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_app/ejemplares/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <div>Ejemplares</div>
}
