package com.fileshare.file.util;

/**
 * Created with IntelliJ IDEA.
 * User: madger
 * Date: 12.06.13
 * Time: 23:15
 * To change this template use FileSystem | Settings | FileSystem Templates.
 */

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileComparator {
    private final File original;
    private final File revised;

    public FileComparator(File original, File revised) {
        this.original = original;
        this.revised = revised;
    }

    public List<Chunk> getChangesFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.CHANGE);
    }

    public List<Chunk> getInsertsFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.INSERT);
    }

    public List<Chunk> getDeletesFromOriginal() throws IOException {
        return getChunksByType(Delta.TYPE.DELETE);
    }

    private List<Chunk> getChunksByType(Delta.TYPE type) throws IOException {
        final List<Chunk> listOfChanges = new ArrayList<>();
        final List<Delta> deltas = getDeltas();
        for (Delta delta : deltas) {
            if (delta.getType() == type) {
                listOfChanges.add(delta.getRevised());
            }
        }
        return listOfChanges;
    }

    private List<Delta> getDeltas() throws IOException {

        final List<String> originalFileLines = fileToLines(original);
        final List<String> revisedFileLines = fileToLines(revised);

        final Patch patch = DiffUtils.diff(originalFileLines, revisedFileLines);

        return patch.getDeltas();
    }

    private List<String> fileToLines(File file) throws IOException {
        final List<String> lines = new ArrayList<>();
        String line;
        final BufferedReader in = new BufferedReader(new FileReader(file));
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }
}
