
# Clínica Veterinária — API REST (ac2_atividade2)

**Aluno:** Márcio Soares de Brito  
**RA:** 248888  
**Instituição:** Centro Universitário Facens (UniFacens)

API REST em Spring Boot para gerenciar uma clínica veterinária: tutores, animais, veterinários, agendamento de consultas, prontuário (atendimentos) e vacinação.

## Tecnologias

- Java 21
- Spring Boot 4.1.2 (Spring Web MVC e Spring Data JPA)
- Banco de dados H2 em memória (os dados são apagados ao reiniciar a aplicação)
- Maven (Maven Wrapper incluso: `mvnw` / `mvnw.cmd`)

## Como executar

### Pré-requisitos

- JDK 21 instalado (`java -version` deve mostrar a versão 21)
- Nenhuma instalação de Maven ou de banco de dados é necessária

### Passos

1. Copie a pasta `src` desta entrega para a raiz do projeto, ao lado do `pom.xml`, substituindo a existente.
2. Na raiz do projeto, execute:

   ```bash
   # Linux / macOS
   ./mvnw spring-boot:run

   # Windows
   mvnw.cmd spring-boot:run
   ```

   Também é possível rodar a classe `Ac2Atividade2Application` direto pela IDE.
3. A API fica disponível em **http://localhost:8081**.

### Console do banco H2 (opcional)

- URL: http://localhost:8081/h2-console
- JDBC URL: `jdbc:h2:mem:clinicadb`
- Usuário: `sa` — Senha: (vazia)

## Estrutura do projeto

```
src/main/java/com/example/ac2_atividade2/
├── controller/   # endpoints REST
├── service/      # regras de negócio e validações
├── repository/   # Spring Data JPA, incluindo as consultas personalizadas (@Query)
├── model/        # entidades JPA
├── dto/          # objetos de entrada/saída
└── exception/    # exceções e tratamento global de erros
```

## Endpoints

| Recurso | Endpoints |
|---|---|
| Tutores | `POST /tutores`, `GET /tutores`, `GET /tutores/{id}`, `PUT /tutores/{id}`, `GET /tutores/{id}/animais` |
| Animais | `POST /animais`, `GET /animais`, `GET /animais/{id}`, `PUT /animais/{id}` |
| Veterinários | `POST /veterinarios`, `GET /veterinarios`, `GET /veterinarios/{id}`, `PUT /veterinarios/{id}` |
| Consultas | `POST /consultas`, `GET /consultas`, `GET /consultas/{id}`, `PUT /consultas/{id}/cancelar` |
| Atendimentos | `POST /atendimentos`, `GET /atendimentos/{id}`, `GET /atendimentos/{id}/imprimir` (HTML para impressão) |
| Vacinas | `POST /vacinas` |
| Histórico do animal | `GET /animais/{id}/atendimentos`, `GET /animais/{id}/vacinas`, `GET /animais/{id}/historico` |

Datas usam o formato `yyyy-MM-dd` e horários usam `HH:mm`.

### Exemplos de requisição

```json
POST /tutores
{ "nome": "Maria Silva", "cpf": "12345678901", "telefone": "15999990000", "endereco": "Rua das Flores, 100" }

POST /animais
{ "nome": "Rex", "especie": "Cachorro", "raca": "Labrador", "dataNascimento": "2020-05-10", "tutorId": 1 }

POST /veterinarios
{ "nome": "Dra. Ana Lima", "crmv": "SP-12345", "especies": ["cachorro", "gato"] }

POST /consultas
{ "animalId": 1, "veterinarioId": 1, "local": "Sala 1", "data": "2026-12-10", "horario": "10:00" }

POST /atendimentos
{ "consultaId": 1, "diagnostico": "Otite leve", "observacoes": "Animal agitado", "orientacoes": "Limpar o ouvido 2x ao dia" }

POST /vacinas
{ "animalId": 1, "nome": "V10", "data": "2026-12-10", "lote": "L2026-045" }
```

## Regras de negócio

- Tutor possui nome, CPF (11 dígitos, único), telefone e endereço; um tutor pode ter vários animais.
- Veterinário possui CRMV único e a lista de espécies que pode atender.
- Um veterinário não pode ter duas consultas no mesmo dia e horário (consultas canceladas liberam o horário).
- A consulta só pode ser agendada se o veterinário for habilitado para a espécie do animal.
- Não é permitido agendar consultas no passado.
- Só consultas `AGENDADA` podem ser canceladas.
- Cada consulta tem no máximo um atendimento, e consultas canceladas não recebem atendimento. Ao registrar o atendimento, a consulta passa para `REALIZADA`.
- Vacinas registram nome, data e lote.

### Códigos de erro

| Código | Quando ocorre |
|---|---|
| 400 | JSON inválido ou data/horário em formato incorreto |
| 404 | Recurso não encontrado |
| 422 | Violação de regra de negócio ou campo obrigatório ausente |

## Consultas personalizadas nos repositórios (`@Query`)

| Repositório | Método | Endpoint |
|---|---|---|
| `AnimalRepository` | `buscarPorNome` | `GET /animais/busca?nome=rex` |
| `VeterinarioRepository` | `buscarPorEspecie` | `GET /veterinarios/especie/gato` |
| `ConsultaRepository` | `agendaPorPeriodo` | `GET /consultas/agenda?inicio=2026-12-01&fim=2026-12-31&status=AGENDADA` |
| `VacinaRepository` | `aplicadasNoPeriodo` | `GET /vacinas?inicio=2026-01-01&fim=2026-12-31` |
| `TutorRepository` | `comPeloMenosAnimais` | `GET /tutores/com-animais?minimo=2` |

## Testes no Postman


### Prints dos testes realizados

**Tutores — criação com sucesso (201) e CPF duplicado (422)**

<img width="882" height="246" alt="Captura de tela 2026-11-04 202652" src="https://github.com/user-attachments/assets/594f4a19-2afc-4f72-9255-bbdf3280051c" />


**Tutores — id inexistente (404) e atualização (200); animais — criação (201)**


<img width="884" height="465" alt="Captura de tela 2026-11-04 202710" src="https://github.com/user-attachments/assets/c58e374d-b24e-456d-9d15-395250eb0f9f" />


**Consultas — agendamento (201), conflito de horário (422), espécie não habilitada (422) e data no passado (422)**

<img width="887" height="500" alt="Captura de tela 2026-11-04 202729" src="https://github.com/user-attachments/assets/45eb84e8-aa08-4d32-ae77-6619b65bacf8" />

