# ender-chest
Plugin simples de EnderChest para servidores que querem dar como vantagem a jogadores VIP um baú do fim que expanda conforme o seu vip.

# Features do plugin
- Delay para abertura do baú do fim (para evitar possíveis dups)
- Bloqueador de abertura utilizando freecam (para evitar dup)
- O baú do fim não perde itens caso o player troque de vip, os itens que não couberem no novo baú são enviados para o seu inventário

# Dependências
O plugin em si não há dependência de plugins externos, porém, é necessário que a máquina em que o plugin rodar tenha MongoDB. (https://www.mongodb.com/)

# Permissões
enderchest.admin - Permissões administrativar. (Abrir o baú de outro de player, e utilizar o /ec toggle)
enderchest.<número de linhas> - Permissões para expandir o baú (Todos começam com 3 linhas, o limite de linhas é 6)
