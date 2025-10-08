package com.switajski.snapshotassert;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.internal.Strings;

public class SnapshotAssert extends AbstractCharSequenceAssert<SnapshotAssert, String> {
  final Strings strings = Strings.instance();
  final boolean updateSnapshot;

  public SnapshotAssert(String actual) {
    super(actual, SnapshotAssert.class);
    updateSnapshot = Boolean.parseBoolean(System.getenv("UPDATE_SNAPSHOT"));
  }

  /**
   * Expects actual to be the same as in the file provided. Given file is commited to git.<br>
   * <br>
   * If change is intended, update the file
   *
   * <ul>
   *   <li>manually
   *   <li>by deleting given file and rerunning the test
   *   <li>by using {@link com.puma.plm.hive.readmodel.utls.SnapshotAssert#updateSnapshot} and
   *       rerunning the test
   * </ul>
   *
   * @param file
   * @return
   */
  public SnapshotAssert isEqualToLastCommit(Path file) {
    Objects.requireNonNull(file);
    try {
      if (updateSnapshot) {
        updateSnapshotWithoutFailing(file);
      } else {
        if (file.toFile().exists()) {
          strings.assertEqualsIgnoringWhitespace(this.info, actual, Files.readString(file));
        } else {
          file.toFile().createNewFile();
          updateSnapshotWithoutFailing(file);
          this.failWithMessage(String.format("No SNAPSHOT file %s found, will create one.", file));
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  /**
   * Run this method to update the snapshot file
   *
   * @param file
   * @return
   * @throws FileNotFoundException
   */
  public SnapshotAssert updateSnapshot(Path file) throws FileNotFoundException {
    updateSnapshotWithoutFailing(file);
    this.failWithMessage(
        "This method is intended to update snapshots and not for assertions. Did you forget to switch back to isEqualToLastCommit?");
    return this;
  }

  private void updateSnapshotWithoutFailing(Path file) throws FileNotFoundException {
    try (PrintWriter ou = new PrintWriter(new FileOutputStream(file.toFile()))) {
      ou.println(actual);
    }
  }
}
