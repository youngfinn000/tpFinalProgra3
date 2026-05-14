c ## 🌿 Estructura de ramas

* `main` → versión estable del proyecto (NO trabajar directamente)
* `develop` → integración de funcionalidades
* `feature/*` → nuevas funcionalidades
* `bugfix/*` → corrección de errores

---

## 🚀 Flujo de trabajo

### 2. Cambiar a la rama develop

```bash
git checkout develop
```

(Si no existe localmente)

```bash
git checkout -b develop origin/develop
```

---

### 3. Crear una nueva rama para trabajar

```bash
git checkout -b feature/nombre-funcionalidad
```

Ejemplo:

```bash
git checkout -b feature/login
```

---

### 4. Guardar cambios

```bash
git add .
git commit -m "Descripción clara del cambio"
```

---

### 5. Subir la rama

```bash
git push origin feature/nombre-funcionalidad
```

---

### 6. Mantener la rama actualizada

Antes de terminar:

```bash
git checkout develop
git pull origin develop
git checkout feature/nombre-funcionalidad
git merge develop
```

---

### 7. Crear Pull Request

En GitHub:

* Ir a la rama subida
* Crear Pull Request hacia `develop`
* Esperar revisión

---

### 8. Merge

Una vez aprobado:

* Se hace merge a `develop`
* Se elimina la rama

---

## ⚠️ Reglas importantes

* ❌ NO trabajar directamente en `main`
* ❌ NO hacer push directo a `develop` sin revisión
* ✅ Usar ramas `feature/*`
* ✅ Hacer commits claros y pequeños
* ✅ Actualizar la rama antes de hacer PR

---

## 🔥 Manejo de conflictos

Si Git marca conflictos:

1. Revisar los archivos
2. Elegir qué código dejar
3. Guardar cambios

```bash
git add .
git commit -m "Resolución de conflicto"
```

---

## 🧠 Buenas prácticas

* Hacer `git pull` seguido
* No acumular muchos cambios sin subir
* Nombrar bien las ramas
* Revisar antes de mergear

---

## 📌 Resumen rápido

1. `clone`
2. `checkout develop`
3. `checkout -b feature/...`
4. `commit`
5. `push`
6. `pull + merge develop`
7. `pull request`
8. `merge`

---

## 👥 Trabajo en equipo

* Todos trabajan con ramas propias
* El código se revisa antes de integrarse
* Se evita pisar el trabajo de otros

---

## 🛑 Importante

Si alguien hace un **push --force**:

* Avisar al equipo
* Todos deben actualizar su repositorio

---

## 📎 Herramientas utilizadas

* Git
* GitHub

---

**Seguir esta guía evita errores y facilita el trabajo en equipo.**
