# Diário de Atividades - TMS 2025/2
## Aula 3 - Controle de Versões 

**Data:** 11/05/2026 - 20/05/2026

---

# Aula 3.1 - Branches e Merge


## Atividade 1: Resolução de Conflitos

### Passo 1: Criar repositório de testes

```bash
mkdir teste-conflitos
cd teste-conflitos
git init

cat > README.md << 'EOF'
# Projeto de Teste

## Orientações Iniciais
Este é um projeto para testar conflitos no Git.

## Como usar
1. Clone o repositório
2. Crie uma branch para suas alterações
3. Faça merge quando finalizar
EOF

git add README.md
git commit -m "Adiciona README com orientações iniciais"
```
![alt text](imagens/image.png)

### Passo 2: Criar ramo de desenvolvimento

```bash
git checkout -b dev

cat > README.md << 'EOF'
# Projeto de Teste

## Orientações Iniciais
Este é um projeto para testar conflitos no Git.

## Como usar
1. Clone o repositório
2. Crie uma branch para suas alterações
3. Faça merge quando finalizar

## Orientações Complementares (adicionadas em dev)
- Sempre faça commits pequenos e frequentes
- Use mensagens descritivas
- Teste antes de fazer merge
EOF

git add README.md
git commit -m "Complementa orientações no README (branch dev)"
```
![alt text](imagens/image1.png)

### Passo 3: Alterar README no ramo principal

```bash
git checkout main

cat > README.md << 'EOF'
# Projeto de Teste

## Orientações Iniciais
Este é um projeto para testar conflitos no Git.

## Como usar
1. Clone o repositório
2. Crie uma branch para suas alterações
3. Faça merge quando finalizar

## Notas Importantes (adicionadas em main)
- Mantenha o código limpo
- Siga as convenções do projeto
EOF

git add README.md
git commit -m "Adiciona notas importantes no README (branch main)"
```
![alt text](imagens/image-1.png)
![alt text](imagens/image-2.png)

### Passo 4: Verificar estado antes do merge

```bash
git log --oneline --decorate --graph --all
```
![alt text](imagens/image-3.png)

### Passo 5: Tentar fazer merge

```bash
git merge dev
```
![alt text](imagens/image-4.png)

### Passo 6: Verificar estado do conflito

```bash
git status

```

![alt text](imagens/image-5.png)

### Passo 7: Examinar o arquivo com conflito

```bash
cat README.md

```
![alt text](imagens/image-6.png)

### Passo 8: Resolver conflito manualmente

```bash
# Editar arquivo para manter ambas as contribuições
cat > README.md << 'EOF'
# Projeto de Teste

## Orientações Iniciais
Este é um projeto para testar conflitos no Git.

## Como usar
1. Clone o repositório
2. Crie uma branch para suas alterações
3. Faça merge quando finalizar

## Orientações Complementares
- Sempre faça commits pequenos e frequentes
- Use mensagens descritivas
- Teste antes de fazer merge

## Notas Importantes
- Mantenha o código limpo
- Siga as convenções do projeto
EOF

# Adicionar e completar merge
git add README.md
git commit -m "Resolve conflito: integra orientações de dev e main"
```
![alt text](imagens/image-7.png)


### Estado final do repositório

```bash
git log --oneline --decorate --graph --all

```
![alt text](imagens/image-8.png)

---


# Aula 3.2 - Git Katas: Branches e Merge

## Atividade 1: Basic Branching

**Repositório:** https://github.com/eficode-academy/git-katas/blob/master/basic-branching/README.md

### Exercícios realizados:

```bash
cd git-katas/basic-branching
source setup.sh

# 1 Listar branches
git branch
# Saída: * master

# 2 Criar nova branch
git branch feature

# 3 Listar novamente
git branch

# 4 Alternar para feature
git checkout feature

# 5 Fazer alterações
echo "Nova funcionalidade" >> file.txt
git add file.txt
git commit -m "Adiciona nova funcionalidade"

# 6 Voltar ao master
git checkout main

# 7 Verificar que file.txt não tem a alteração
cat file.txt

# 8 Ver histórico
git log --oneline --all --graph
```
![alt text](imagens/image-9.png)
![alt text](imagens/image-10.png)

### Conceitos praticados:
- Criação de branches
- Alternância entre branches
- Isolamento de alterações

---
![alt text](imagens/image-13.png)
![alt text](imagens/image-14.png)
![alt text](imagens/image-15.png)
![alt text](imagens/image-16.png)
![alt text](imagens/image-17.png)
![alt text](imagens/image-18.png)

## Atividade 2: Fast-Forward Merge

**Repositório:** https://github.com/eficode-academy/git-katas/blob/master/ff-merge/README.md

### Exercícios realizados:

```bash
cd git-katas/ff-merge
source setup.sh

# 1. Ver estado inicial
git log --oneline --all --graph

# 2. Verificar branches
git branch

# 3. Fazer merge fast-forward
git checkout master
git merge feature

# 4. Ver histórico após merge
git log --oneline --all --graph
```
![alt text](imagens/image-11.png)
![alt text](imagens/image-12.png)
![alt text](imagens/image-19.png)
![alt text](imagens/image-20.png)
![alt text](imagens/image-21.png)
![alt text](imagens/image-22.png)

### Conceito de Fast-Forward:
```
ANTES:                    DEPOIS:
master                    master, feature
   |                           |
   v                           v
   A---B                   A---B---C
       |
     feature
       |
       v
       C
```

---

## Atividade 3: 3-Way Merge

**Repositório:** https://github.com/eficode-academy/git-katas/blob/master/3-way-merge/README.md

### Exercícios realizados:

```bash
cd ../3-way-merge
source setup.sh
cd exercise

git switch -c greeting
echo "Oi, tudo bem?" > greeting.txt
git add greeting.txt
git commit -m "Change greeting"

git switch master
printf "# Repo de teste\nKata 3-way merge\n" > README.md
git add README.md
git commit -m "Add README"

git log --oneline --graph --all
git diff master greeting
git merge greeting
git log --oneline --graph --all

```
![alt text](imagens/image-23.png)
![alt text](imagens/image-24.png)
![alt text](imagens/image-25.png)
![alt text](imagens/image-26.png)


---

## Atividade 4: Merge Conflict

**Repositório:** https://github.com/eficode-academy/git-katas/blob/master/merge-conflict/README.md

### Exercícios realizados:

```bash
cd ../merge-conflict
source setup.sh
cd exercise

git merge merge-conflict-branch1
git status
cat file.txt
git add file.txt
git commit
git log --oneline --graph

```
![alt text](imagens/image-27.png)
![alt text](imagens/image-28.png)


---

## Atividade 5: Merge-Mergesort

**Repositório:** https://github.com/eficode-academy/git-katas/blob/master/merge-mergesort/README.md

### Exercícios realizados:

```bash
cd ../merge-mergesort
source setup.sh
cd exercise

git branch
git merge Mergesort-Impl
git status
cat mergesort.py
git add mergesort.py
git commit
git status

```

---
![alt text](imagens/image-29.png)
![alt text](imagens/image-30.png)
![alt text](imagens/image-31.png)
![alt text](imagens/image-32.png)

## Atividade 6: Rebase

**Repositório:** https://github.com/eficode-academy/git-katas/blob/master/rebase-branch/README.md

### Exercícios realizados:

```bash
cd ../rebase-branch
source setup.sh
cd exercise

git branch
git log --oneline --graph --all

git switch uppercase
git log --oneline --graph --all

git rebase master
git log --oneline --graph --all

git switch master
git merge uppercase
git log --oneline --graph --all

```
![alt text](imagens/image-33.png)
![alt text](imagens/image-34.png)
![alt text](imagens/image-35.png)


---

## Registro de Progresso

### O que foi feito:
- [x] Estudo de branches 
- [x] Prática de merge 
- [x] Resolução de conflitos manual e com ferramenta
- [x] Gestão de ramos 
- [x] Fluxo de trabalho com branches
- [x] Git Katas: basic-branching
- [x] Git Katas: ff-merge
- [x] Git Katas: 3-way-merge
- [x] Git Katas: merge-conflict
- [x] Git Katas: merge-mergesort
- [x] Git Katas: rebase-branch


### O que ficou pendente:
- [ ] Configurar múltiplos remotos em projeto real

### O que impediu:
- Nenhum impedimento

### O que será feito na sequência:
- Estudar repositórios remotos

---

## Referências

- [Git Katas](https://github.com/eficode-academy/git-katas)


