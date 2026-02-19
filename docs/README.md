# Natto User Guide

Natto is a simple task manager that helps you track todos, deadlines, and events using easy commands.

![Ui.png](Ui.png)

## Quick start

1. Download the latest `Natto.jar`.
2. Run it:

   ```bash
   java -jar Natto.jar
    ```

## Command format

- Words in `UPPER_CASE` are placeholders you replace.
- Commands are **case-sensitive** (use lowercase like `todo`, `list`, `mark`, etc.)

Example format:

`keyword ARGUMENTS`

---

## Features

### Listing all tasks: `list`

Shows all tasks currently stored.

Format:  
`list`

Example:  
`list`

Expected output (example):
Here are the tasks in your list:

[T][ ] read book


---

### Adding a todo: `todo`

Adds a basic task with no date/time.

Format:  
`todo DESCRIPTION`

Example:  
`todo buy milk`

Expected output (example):
Meow~😺. I've added this task:
[T][ ] buy milk
Now you have 1 tasks in the list.

---

### Adding a deadline: `deadline`

Adds a task with a due date.

Format:  
`deadline DESCRIPTION /by YYYY-MM-DD`  
or  
`deadline DESCRIPTION /by YYYY-MM-DD HHmm`

Examples:  
`deadline submit report /by 2026-03-10`  
`deadline submit report /by 2026-03-10 2359`

Expected output (example):
Meow~😺. I've added this task:
[D][ ] submit report (by: Mar 10 2026)
Now you have 2 tasks in the list.


---

### Adding an event: `event`

Adds a task with a start and end time.

Format:  
`event DESCRIPTION /from YYYY-MM-DD HHmm /to HHmm`  
or  
`event DESCRIPTION /from YYYY-MM-DD HHmm /to YYYY-MM-DD HHmm`

Examples:  
`event team meeting /from 2026-03-01 1400 /to 1600`  
`event camp /from 2026-03-01 0900 /to 2026-03-03 1800`

Expected output (example):
Meow~😺. I've added this task:
[E][ ] team meeting (from: Mar 01 2026 14:00 to: 16:00)
Now you have 3 tasks in the list.

---


### Marking a task as done: `mark`

Marks a task as completed using its index from `list`.

Format:  
`mark INDEX`

Example:  
`mark 1`

Expected output (example):
Meow~😺! I've marked this task as done:
[T][X] buy milk


---

### Unmarking a task: `unmark`

Marks a completed task as not done.

Format:  
`unmark INDEX`

Example:  
`unmark 1`

Expected output (example):
Meow~😺, I've marked this task as not done yet:
[T][ ] buy milk

---

### Deleting a task: `delete`

Deletes a task by its index.

Format:  
`delete INDEX`

Example:  
`delete 2`

Expected output (example):
Meow~😺. I've removed this task:
[D][ ] submit report (by: Mar 10 2026)
Now you have 2 tasks in the list.

---

### Finding tasks by keyword: `find`

Searches and displays tasks containing the keyword.

Format:  
`find KEYWORD`

Example:  
`find report`

Expected output (example):
Meow~😺. Here are the matching tasks in your list:

[D][ ] submit report (by: Mar 10 2026)

---

### Adding a contact: `contact`

Adds a contact entry.

Format:  
`contact NAME p/PHONE e/EMAIL [a/ADDRESS]`

Examples:  
`contact Alex p/91234567 e/alex@gmail.com`  
`contact Ben p/98765432 e/ben@gmail.com a/NUS`

Expected output (example):
Meow~😺. I've added this task:
[C][ ] Ben (p: 98765432, e: ben@gmail.com
, a: NUS)
Now you have 4 tasks in the list.

---

### Viewing creator contact: `creator`

Shows the creator’s contact info.

Format:  
`creator`

Expected output (example):
🐱Creator🐱: Kemango
Contact: 8283 6964
Email: e1398747@u.nus.edu

---

### Exiting the program: `bye`

Exits the app.

Format:  
`bye`

Expected output (example):
Bye. Hope to see you again soon!😺

---

## Error handling

If you enter an invalid command, Natto will show an error message.

Example:  
`mark abc`

Expected output (example):
Index must be a number.
Meow!! 🙀
