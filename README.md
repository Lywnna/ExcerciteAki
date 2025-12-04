# ExerciteAki

Sistema console em Java para gerenciamento de academia: cadastro de alunos, instrutores, aparelhos, treinos, evolução física e frequência, com persistência em arquivos CSV.

## 🛠️ Tecnologias

- Java 17+
- Execução via console
- Persistência em arquivos `.csv`

## 🚀 Como executar

1. Clone ou baixe o projeto.
2. Abra o projeto na sua IDE de preferência.
3. Execute a classe `view.GymSystemMenu` (método `main`).
4. Na primeira execução os arquivos `.csv` em `src/Tables` são criados automaticamente.

## 📚 Estrutura

- `src/model`: entidades (Gym, Address, Instructor, Member, Equipment, Training, Exercise, Progress, Attendance, Schedule, Weekday, User, Administrator).
- `src/database`: persistência em CSV (`DataPersistence`).
- `src/view`: interface de console (`GymSystemMenu`).
- `src/Tables`: arquivos `.csv` com os dados.

Principais arquivos CSV:
- `gym.csv`, `schedules.csv`
- `instructors.csv`, `members.csv`
- `equipments.csv`, `exercises.csv`
- `progress.csv`, `attendance.csv`

## ⚙️ Funcionalidades

**Administrador**
- Configurar dados da academia e horários.
- Cadastrar / editar / excluir / consultar instrutores.
- Cadastrar / editar / excluir / consultar alunos.
- Cadastrar / editar / excluir / consultar aparelhos.

**Instrutor**
- Definir / alterar treino do aluno por dia da semana.
- Registrar evolução (peso e % de massa muscular) do aluno.

**Aluno**
- Consultar treino do dia.
- Ver histórico de evolução.
- Registrar entrada e saída (frequência).
- Ver relatório de frequência (últimas visitas / por período).

## 👨‍💻 Autores

- Andrei Fulcher Ribeiro
- Luna
- Projeto acadêmico de Programação Orientada a Objetos (Java).
