# Milo User Guide

![Milo UI Screenshot](docs/images/your-screenshot.png)

> Replace the image path above with your actual screenshot inside `/docs/images`.

---

## 🚀 Welcome to Milo

**Milo** is a sleek, automated task management assistant designed for students and professionals who prefer a fast, command-line style interface within a modern Graphical User Interface.

Milo helps you track deadlines, events, and daily to-dos with ease — all through simple, powerful commands.

---

# 📅 Adding Deadlines

Adds a task that must be completed by a specific date.

Milo tracks the date automatically and allows you to filter or sort tasks based on deadlines.

## 📝 Format

deadline DESCRIPTION /by YYYY-MM-DD


## ✅ Example

deadline submit project /by 2026-02-20


## 💡 Expected Output

Got it. I've added this task:
[D][ ] submit project (by: Feb 20 2026)
Now you have 5 tasks in the list.


---

# 📊 Feature: Intelligent Sorting (C-Sort)

Milo allows you to reorganize your task list instantly without manually moving tasks.

This is especially useful when your list grows long.

---

## 1️⃣ Sort by Name

Arranges all tasks alphabetically based on their description.

### Command

sort name


### Outcome

Your current list is reordered from **A to Z**.

---

## 2️⃣ Sort by Date

Arranges Deadlines and Events chronologically.

### Command

sort date


### Outcome

- Tasks occurring sooner appear at the top  
- Todo tasks (which have no dates) are automatically moved to the bottom  

This keeps your schedule clear and organized.

---

# 🔍 Feature: Date-Specific Search

Find exactly what is happening on a specific day without scrolling through your entire task list.

## 📝 Format

find-date YYYY-MM-DD


## ✅ Example

find-date 2026-02-18


## 💡 Expected Output

Here are the tasks occurring on Feb 18 2026:
1.[E][ ] project meeting (from: Feb 18 2026 to: Feb 18 2026)


---

# 💾 Feature: Automated Persistence

You never have to click **Save**.

Milo is designed with a robust storage system that updates your local data file after every single command.

## 🔧 How It Works

Every time you:

- Add a task  
- Delete a task  
- Mark a task  

Milo writes the current state to:

./data/milo.txt


## ✅ Result

Even if the app crashes or closes unexpectedly, your data remains safe and will be reloaded the next time you start Milo.

---

# 🎯 Why Milo?

- ⚡ Fast command-based interaction  
- 🧠 Smart sorting and filtering  
- 💾 Automatic saving  
- 🎓 Designed for students & professionals  

---

## 📌 Hosting on GitHub Pages

This User Guide is hosted via GitHub Pages using the `/docs` folder.

To update:
1. Place screenshots inside `/docs/images`
2. Update the image path at the top of this file
3. Commit and push changes

---

**Milo — Simple. Fast. Reliable.**