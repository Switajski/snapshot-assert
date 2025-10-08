## Project Overview

This is a Java library that provides snapshot assertion functionality for testing. The core class `SnapshotAssert` extends AssertJ's `AbstractCharSequenceAssert` to enable snapshot-based testing where test outputs are captured and compared against saved snapshots.

## Snapshot Testing Workflow

The library supports snapshot testing through environment variable control:

- **UPDATE_SNAPSHOT=true**: Updates snapshot files instead of asserting against them
- **Normal mode**: Compares actual output against existing snapshot files

Key methods in `SnapshotAssert`:
- `isEqualToLastCommit(Path file)`: Main assertion method that compares against snapshot file
- `updateSnapshot(Path file)`: Explicitly updates snapshot and fails test (for development workflow)

# Testing using Snapshots

Snapshot testing is a software testing technique that captures the current state (given messages) of a system
and saves it in a file e.g. in JSON/HTML/TXT. Subsequent test runs can then compare the current output with the saved snapshot to detect any unintended changes.

Here's a basic workflow for snapshot testing:

- Initial Test Run: The first time you run a test, the output is captured and saved as a snapshot.

- Subsequent Test Runs: In subsequent test runs, the current output is compared to the saved snapshot.

- Test Pass/Fail Decision: If the current output matches the snapshot, the test passes. If there are differences, the test
fails, indicating that the code changes may have introduced unintended consequences.


Snapshot testing is commonly used in UI testing, where the output is often visual (e.g., web pages, user interfaces).
When a test fails, developers can review the differences between the current output and the snapshot
to decide whether the changes are expected or if there is a problem in the code.