# 🚗 DriveRent

Sistema de gerenciamento de locação de veículos desenvolvido em Java.

O DriveRent foi desenvolvido como um projeto acadêmico com o objetivo de aplicar conceitos de Programação Orientada a Objetos, separação de responsabilidades, injeção de dependências, tratamento de exceções, persistência de dados e modelagem de um sistema de locação de veículos.

---

## 📋 Sobre o projeto

O DriveRent permite gerenciar clientes, veículos e locações através de uma aplicação executada pelo terminal.

O sistema possui três áreas principais:

- 👤 Gerenciamento de clientes
- 🚗 Gerenciamento de veículos
- 📋 Gerenciamento de locações

Além disso, o sistema possui regras de negócio para disponibilidade de veículos, cálculo de valores de locação, multas por atraso, cancelamentos, devoluções e pagamentos.

Os dados são persistidos localmente em arquivos JSON, permitindo que as informações sejam mantidas mesmo após o encerramento da aplicação.

---

## 🛠️ Tecnologias utilizadas

- **Java 21**
- **Maven**
- **Gson**
- **JSON**
- **Git**
- **GitHub**

### Conceitos e recursos utilizados

- Programação Orientada a Objetos (POO)
- Encapsulamento
- Herança
- Abstração
- Polimorfismo
- Interfaces
- Generics
- Streams
- Enumerações
- Injeção de dependências
- Separação de responsabilidades
- Tratamento de exceções
- Serialização e desserialização JSON

---

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura em camadas, buscando separar as responsabilidades de cada parte do sistema.

```text
src/
├── model/
│   ├── entities/
│   └── enums/
├── dao/
├── service/
├── persistence/
├── exception/
└── view/
```

### Responsabilidades das camadas

**Model**

Contém as entidades e enumerações utilizadas para representar o domínio da aplicação.

**DAO**

Responsável pelo acesso e manipulação dos dados persistidos.

**Service**

Contém as regras de negócio, validações e operações principais do sistema.

**Persistence**

Responsável pelo gerenciamento da persistência dos dados em arquivos JSON utilizando Gson.

**Exception**

Contém as exceções personalizadas utilizadas para representar erros específicos do domínio.

**View**

Responsável pela interação com o usuário através do terminal.

---

## 💾 Persistência de dados

O sistema utiliza arquivos JSON para persistir os dados localmente.

Os arquivos são armazenados no diretório:

```text
data/
├── clientes.json
├── veiculos.json
└── locacoes.json
```

A leitura e escrita dos arquivos são centralizadas através da classe `JsonDataManager`.

O projeto também utiliza adapters personalizados do Gson para lidar com tipos que necessitam de tratamento específico durante a serialização e desserialização, como:

- `LocalDate`
- Herança entre os diferentes tipos de `Veiculo`

Dessa forma, os dados permanecem disponíveis mesmo após o encerramento da aplicação.

---

## ✨ Funcionalidades

### 👤 Clientes

O sistema permite:

- Cadastrar clientes
- Buscar cliente por CPF
- Listar todos os clientes
- Atualizar dados do cliente
- Remover cliente
- Validar CPF
- Validar nome
- Validar telefone
- Validar CNH
- Impedir a remoção de clientes que possuem locações ativas

Os dados do cliente incluem:

- Nome
- CPF
- CNH
- Telefone

CPF, CNH e telefone são tratados como `String`, pois são identificadores e dados de contato, não valores utilizados para operações matemáticas.

---

### 🚗 Veículos

O sistema suporta diferentes categorias de veículos:

- 🏍️ Moto
- 🚘 Carro de passeio
- 🚚 Utilitário

Cada tipo possui características e regras específicas.

#### 🏍️ Moto

Possui:

- Cilindradas
- Valor diário
- Regra específica para cálculo da diária

Motos com mais de 250 cilindradas possuem um desconto de 5% no cálculo da diária.

#### 🚘 Carro de passeio

Possui:

- Número de portas
- Ar-condicionado
- Valor diário

Veículos com ar-condicionado possuem um acréscimo de R$ 20,00 no cálculo da diária.

#### 🚚 Utilitário

Possui:

- Capacidade de carga em toneladas
- Valor diário

A capacidade de carga influencia o cálculo da diária.

---

## 🔧 Gerenciamento de veículos

É possível:

- Cadastrar veículo
- Buscar veículo por placa
- Listar veículos
- Atualizar veículo
- Alterar status
- Remover veículo

Os veículos possuem diferentes estados, como:

```text
DISPONIVEL
ALUGADO
EM_MANUTENCAO
```

O sistema também possui validações para:

- Placa
- Ano
- Valor da diária
- Cilindradas
- Número de portas
- Capacidade de carga

---

## 📋 Locações

O sistema permite:

- Cadastrar locação
- Buscar locações por cliente
- Buscar locações por veículo
- Listar todas as locações
- Atualizar a data de término
- Trocar o veículo de uma locação
- Finalizar locação
- Cancelar locação
- Deletar locação
- Registrar pagamento

Ao cadastrar uma locação:

1. O cliente é verificado.
2. O veículo é verificado.
3. A disponibilidade do veículo é verificada.
4. A data da locação é definida.
5. O valor base da locação é calculado.
6. A locação recebe o status `ATIVA`.
7. O veículo passa para o status `ALUGADO`.

---

## 🔄 Troca de veículo

Durante uma locação ativa é possível trocar o veículo.

O sistema:

- Verifica se a locação está ativa.
- Verifica se o novo veículo existe.
- Verifica se o novo veículo está disponível.
- Calcula os valores referentes aos períodos com cada veículo.
- Libera o veículo anterior.
- Define o novo veículo como alugado.

---

## 🏁 Finalização da locação

Ao finalizar uma locação, o sistema registra a data de devolução e calcula uma possível multa por atraso.

Caso a devolução ocorra após a data prevista, a multa é adicionada ao valor da locação.

Após a finalização:

```text
Locação: ATIVA → CONCLUIDA
Veículo: ALUGADO → DISPONIVEL
```

---

## 💳 Pagamentos

O sistema possui suporte ao registro de pagamentos associados às locações.

Os pagamentos possuem informações relacionadas ao:

- Valor
- Método de pagamento
- Status do pagamento

Os métodos de pagamento e seus respectivos estados são representados através de `enum`.

---

## 📐 Regras de negócio

O sistema possui diversas regras para garantir a consistência dos dados, incluindo:

- Um CPF não pode estar associado a mais de um cliente.
- Um veículo não pode ser alugado enquanto estiver indisponível.
- Um cliente com locação ativa não pode ser removido.
- A disponibilidade do veículo é verificada antes da criação de uma locação.
- Uma locação ativa pode sofrer troca de veículo seguindo as regras de disponibilidade.
- A finalização da locação altera o estado do veículo.
- Atrasos na devolução podem gerar multas.
- Cada categoria de veículo possui sua própria regra de cálculo de valor.
- Dados inválidos geram exceções específicas do domínio.

---

## ⚠️ Tratamento de exceções

O projeto utiliza exceções personalizadas para representar situações específicas do sistema.

Entre elas:

```text
DadosInvalidosException
DataInvalidaException
EntidadeNaoEncontradaException
LocacaoAtivaException
VeiculoIndisponivelException
```

As exceções são utilizadas principalmente na camada de serviço para impedir operações que violem as regras de negócio.

A camada de apresentação (`View`) realiza o tratamento dessas exceções para informar o usuário sem interromper a execução normal do sistema.

---

## ▶️ Como executar

### Pré-requisitos

Para executar o projeto, é necessário ter instalado:

- Java 21
- Maven

### 1. Clone o repositório

```bash
git clone https://github.com/lazaroaraujo-dev/DriveRent.git
```

### 2. Entre no diretório do projeto

```bash
cd DriveRent
```

### 3. Compile o projeto

```bash
mvn clean package
```

### 4. Execute a aplicação

Execute a classe `Main`.

A aplicação será iniciada através do terminal.

---

## 📁 Estrutura do projeto

```text
DriveRent/
│
├── data/
│   ├── clientes.json
│   ├── veiculos.json
│   └── locacoes.json
│
├── DriveRent-Project/
│   └── src/
│       ├── dao/
│       ├── exception/
│       ├── model/
│       │   ├── entities/
│       │   └── enums/
│       ├── persistence/
│       ├── service/
│       └── view/
│
├── pom.xml
├── .gitignore
└── README.md
```

---

## 📚 Conceitos aplicados

Durante o desenvolvimento foram aplicados conceitos importantes de desenvolvimento de software e Programação Orientada a Objetos, como:

- **Encapsulamento**
- **Herança**
- **Abstração**
- **Polimorfismo**
- **Interfaces**
- **Generics**
- **Streams API**
- **Enum**
- **Injeção de dependências**
- **Separação de responsabilidades**
- **Arquitetura em camadas**
- **Persistência de dados**
- **Serialização e desserialização JSON**
- **Tratamento de exceções**
- **Validação de dados**
- **Regras de negócio**

---

## 🚧 Próximos passos

Algumas melhorias que podem ser implementadas futuramente:

- [ ] Adicionar testes automatizados
- [ ] Melhorar o tratamento de entradas inválidas no terminal
- [ ] Melhorar a interface da aplicação
- [ ] Adicionar mais validações de domínio
- [ ] Migrar a persistência JSON para um banco de dados relacional
- [ ] Criar uma API REST utilizando Spring Boot
- [ ] Adicionar documentação da API
- [ ] Criar uma interface web para o sistema

---

## 👨‍💻 Autor

**Lázaro Araújo**

Projeto desenvolvido para fins acadêmicos e como parte da construção de portfólio na área de desenvolvimento de software.
