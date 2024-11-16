# Citronix: Citrus Farm Management Application

**Welcome to Citronix!**  
Citronix is a citrus farm management application designed to streamline operations for farmers by managing farms, fields, trees, harvests, and sales. The application incorporates advanced constraints to ensure data consistency and optimal productivity.

---

## **Domain-Driven Design (DDD) Structure**

### **1. Domains**
The primary business domains of Citronix are:
- **Farm Management**: Organizes farms and their associated data.
- **Field Management**: Manages fields within farms, including constraints on size and number.
- **Tree Management**: Tracks trees, including planting dates, age, and productivity.
- **Harvest Management**: Oversees seasonal harvests and their details.
- **Sales Management**: Handles sales data, revenue calculations, and client information.

---

### **2. Aggregates**
- **Farm Aggregate**
    - Root Entity: `Farm`
    - Associated Entities: `Field`
- **Field Aggregate**
    - Root Entity: `Field`
    - Associated Entities: `Tree`
- **Tree Aggregate**
    - Root Entity: `Tree`
    - Associated Entities: `HarvestDetail`
- **Harvest Aggregate**
    - Root Entity: `Harvest`
    - Associated Entities: `HarvestDetail`, `Tree`
- **Sales Aggregate**
    - Root Entity: `Sale`
    - Associated Entities: `Harvest`

---

### **3. Bounded Contexts**
- **Farm Context**: Handles CRUD operations for farms and fields, ensuring size constraints.
- **Tree Context**: Manages tree lifecycle, including planting, aging, and productivity calculations.
- **Harvest Context**: Oversees seasonal harvests and ensures trees are only harvested once per season.
- **Sales Context**: Tracks sales and calculates revenues.

---

## **User Stories**

### **Farm Management**
1. **As a farm owner**, I want to create, modify, and view farm details (name, location, size, creation date) to manage my farm efficiently.
2. **As a farm owner**, I want to search for farms using multiple criteria to find specific information quickly.

### **Field Management**
3. **As a farm owner**, I want to associate fields with farms while ensuring the total field area does not exceed the farm's area.
4. **As a farm owner**, I want to ensure no field exceeds 50% of the farm's total area and the minimum size of 0.1 hectares.
5. **As a farm owner**, I want to manage up to 10 fields per farm.

### **Tree Management**
6. **As a farmer**, I want to track trees by planting date, age, and field association to monitor their productivity.
7. **As a farmer**, I want to calculate tree productivity based on their age:
    - Young: 2.5 kg/season.
    - Mature: 12 kg/season.
    - Old: 20 kg/season.
8. **As a farmer**, I want to ensure trees are planted only between March and May for optimal growth.

### **Harvest Management**
9. **As a farmer**, I want to record one harvest per season per field to avoid duplicate entries.
10. **As a farmer**, I want to track the quantity harvested from each tree.

### **Sales Management**
11. **As a farm owner**, I want to record sales details, including the client, date, unit price, and associated harvest.
12. **As a farm owner**, I want to calculate revenue for each sale: `Revenue = Quantity * Unit Price`.

---

## **Workflow Integration with GitHub Actions**

### **Validation Workflow**
Triggered on `feature/*` branches and pull requests to `develop` or `main`.

1. Validates code with `mvn install -DskipTests`.
2. Runs unit tests with `mvn test`.

### **Build and Deploy Workflow**
Triggered on merges to `develop` or `main`.

1. Builds the application and Docker image.
2. Deploys to a staging environment (on `develop`) or production environment (on `main`).
3. Runs integration tests in a Dockerized setup.

---

## **Branching Strategy**

### **Main Branches**
- `main`: Production-ready code.
- `develop`: Integration branch for testing.

### **Feature Branches**
- `feature/EPIC-{number}/{epic-name}`: Major features or epics.
- `feature/US-{number}/{feature-name}`: User stories within an epic.
- `feature/ST-{number}/{subtask-name}`: Subtasks for a user story.

---

## **Commit Message Convention**

Format:
```plaintext
type(scope): subject
[optional body]
[optional footer]
```

**Types**:
- `feat`: New features.
- `fix`: Bug fixes.
- `docs`: Documentation updates.
- `style`: Code style changes.
- `refactor`: Code restructuring.
- `test`: Adding tests.
- `chore`: Maintenance tasks.

**Example**:
```plaintext
feat(farm): implement farm creation feature
- Add form for farm creation
- Include validation for name and size
JIRA: US-1.1
```

---

## **Pull Request Template**

### **Template**
```markdown
## Description  
[Brief description of the changes]

## JIRA Ticket  
- [EPIC-1] Farm Domain Management  
- [US-1.1] Farm Registration  

## Type of Change  
- [ ] New feature  
- [ ] Bug fix  
- [ ] Refactoring  
- [ ] Documentation  

## Testing  
- [ ] Unit tests added  
- [ ] Integration tests added  
- [ ] Manual testing performed  
- [ ] Regression testing performed  

## Checklist  
- [ ] Code follows project style guidelines  
- [ ] Documentation updated  
- [ ] Tests passing  
- [ ] PR is linked to JIRA ticket  
```

---

## **Workflow**

1. **Feature Development**
    - Start with `develop`.
    - Create a branch for the user story:
      ```bash
      git checkout -b feature/US-1.1/farm-registration
      ```

2. **Code Validation**
    - Push code to the feature branch.
    - The validation pipeline runs.

3. **Merging to Develop**
    - Create a pull request to `develop`.
    - Trigger the build-and-deploy pipeline to the staging environment.

4. **Testing and Promotion**
    - After staging tests, create a PR from `develop` to `main`.
    - Trigger deployment to production on merge.

[uml class](umlClass.pdf)