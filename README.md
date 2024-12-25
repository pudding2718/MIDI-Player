# MIDI Player 🎵

A feature-rich Java application for creating, editing, and playing MIDI compositions. It includes an intuitive interface for musicians and hobbyists to explore their creativity.

This project was developed as part of **Whitman College CS370**.

## Features

- **Composition and Playback**: Add, edit, group, and play notes with real-time feedback.
- **Instrument Variety**: Choose from multiple instruments like Piano, Violin, and Guitar.
- **Visual and Interactive UI**: Utilize JavaFX for a polished user experience.
- **File Management**: Save and reload compositions as `.composer` files.
- **Advanced Utilities**: Leverage JSON serialization, undo/redo history, and custom MIDI event handling.

## Tech Stack

- **JavaFX**: For the graphical interface.
- **Maven**: Dependency and build management.
- **Gson**: JSON operations.
- **MIDI Library**: Custom `MidiPlayer` built with `javax.sound.midi`.

## Project Structure

### Core Components

- **`MainApplication`**: The entry point of the application, initializing the JavaFX stage.
- **`MidiPlayer`**: Manages MIDI sequences, adding events, and playback.

### Controllers and Interfaces

- **`Controllers`**:
  - `MainController`, `MenuController`, and `InstrumentsController` handle user interactions and business logic.
- **`Interfaces`**:
  - `INote`, `IComposable`, `IHideable`, and `ISelectable` define core functionality for musical notes and objects.

### Helpers

Utility classes for extended functionality:
- **`FileHelper`**: File operations for `.composer` files.
- **`JsonHelper`**: JSON serialization/deserialization for complex note objects.
- **`NoteHelper`**: Handles note creation, selection, and grouping.
- **`OperationHistoryHelper`**: Tracks undo/redo actions.

### Resources

- **FXML Files**: Define the UI layout for `Main.fxml`, `Menu.fxml`, and `Instruments.fxml`.
- **Module Configuration**: Managed in `module-info.java`.
- **Maven Configuration**: Dependencies and build setup in `pom.xml`.

## Architecture Overview

Below is the class diagram for the MIDI Player project, showcasing the structure and relationships between different components, including controllers, helpers, and interfaces:

![Class Diagram](./class-diagram.png)

## Contributors

- [Tina](https://github.com/tina-github), [Paul](https://github.com/paul-github), [Flora](https://github.com/flora-github), [Jess](https://github.com/jess-github)

