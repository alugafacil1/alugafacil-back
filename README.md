# 🏠 AlugaFácil API

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-IAM-add8e6?style=for-the-badge&logo=keycloak&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FCM-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

Backend da plataforma **AlugaFácil**, uma solução robusta para gestão de aluguel de imóveis. O sistema conecta Inquilinos e Contribuidores (Proprietários/Corretores), gerenciando todo o ciclo de vida do aluguel, desde a busca geolocalizada até a assinatura de planos e notificações em tempo real.

Equipe:
- Luann Ferreira, Inês Alessandra, Carla Daniela, Rodrigo Leandro, Lucas Messias, José Everton, Jenilson Moraes.
---

## 🚀 Stack Tecnológica

* **Linguagem:** Java 21 (LTS)
* **Framework:** Spring Boot 3.x
* **Segurança & IAM:** Keycloak (OAuth2 / OIDC)
* **Banco de Dados:** PostgreSQL
* **Notificações:** Firebase Cloud Messaging (FCM)
* **Mapeamento:** MapStruct
* **Consultas Avançadas:** QueryDSL
* **Testes:** JUnit 5, Mockito, MockMvc
* **Storage:** Cloudinary (Prod) / Local (Dev)

---

## 💼 Funcionalidades e Fluxos

O sistema é dividido em domínios principais para garantir escalabilidade e organização:

* **👥 Gestão de Usuários:** Diferenciação clara entre **Inquilinos** e **Contribuidores** (Proprietários e Corretores), com perfis e permissões gerenciados via Keycloak.
* **🏢 Imóveis:** Cadastro completo com suporte a upload de fotos/vídeos e busca avançada por raio (geolocalização) e filtros dinâmicos.
* **💎 Planos & Assinaturas (SaaS):** Controle de limites de uso (número de imóveis, destaque, vídeos) baseado no plano ativo do usuário. Inclui fluxo de upgrade/downgrade.
* **🤝 Agências:** Módulo para corretores vincularem seus perfis a imobiliárias, gerenciando dados corporativos.
* **🔔 Notificações:** Sistema de alertas push integrados ao Firebase para avisar sobre novos imóveis, contatos e alterações na conta.

---

## ⚙️ Configuração e Instalação

### Pré-requisitos
* JDK 21
* Maven 3.8+
* Docker (recomendado para subir Keycloak e Postgres)

### Variáveis de Ambiente
Configure as variáveis no seu sistema:

| Variável | Descrição |
| :--- | :--- |
| `DB_URL` | URL JDBC do PostgreSQL |
| `DB_USERNAME` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |
| `KEYCLOAK_ISSUER_URI` | URL do Realm no Keycloak |
| `FIREBASE_CREDENTIALS` | Caminho ou JSON da chave de serviço do Firebase |
| `CLOUDINARY_URL` | (Prod) URL de conexão do Cloudinary |

### Rodando a Aplicação

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/alugafacil-back.git](https://github.com/seu-usuario/alugafacil-back.git)
    ```

2.  **Compile o projeto:**
    ```bash
    mvn clean install
    ```

3.  **Execute:**
    ```bash
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    ```

---

## 🧪 Testes Automatizados

O projeto mantém um alto padrão de qualidade com uma suíte de testes automatizados, cobrindo desde regras de negócio isoladas até a integridade dos endpoints da API.

### Tipos de Testes Incluídos:
* **Testes Unitários:** Validam a lógica de negócio nos Services, cálculos de planos, regras de validação e mapeamentos, isolando dependências externas com Mocks.
* **Testes de Integração:** Validam os Controllers, a serialização JSON, os códigos de status HTTP e a interação com a camada de segurança.

### Como Rodar os Testes:

Para executar a suíte completa:

```bash
mvn test
