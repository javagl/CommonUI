/*
 * www.javagl.de - Common - UI
 *
 * Copyright (c) 2013-2019 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.common.ui.text;

import java.util.function.IntBinaryOperator;

/**
 * Utility methods related to strings
 */
class StringUtils
{
    /**
     * A comparator for characters that returns 0 when they are equal ignoring
     * the case, and 1 otherwise.
     */
    private static final IntBinaryOperator IGNORING_CASE = (c0, c1) -> 
    {
        char u0 = Character.toUpperCase((char) c0);
        char u1 = Character.toUpperCase((char) c1);
        if (u0 == u1)
        {
            return 0;
        }
        if (Character.toLowerCase(u0) == Character.toLowerCase(u1))
        {
            return 0;
        }
        return 1;
    }; 

    /**
     * Returns the index of the first appearance of the given target in
     * the given source, starting at the given index. Returns -1 if the 
     * target string is not found.
     * 
     * @param source The source string
     * @param target The target string
     * @param startIndex The start index
     * @param ignoreCase Whether the case should be ignored
     * @return The index
     */
    static int indexOf(String source, String target, 
        int startIndex, boolean ignoreCase)
    {
        if (ignoreCase)
        {
            return indexOf(source, target, startIndex, IGNORING_CASE);
        }
        return indexOf(source, target, startIndex, 
            (c0, c1) -> Integer.compare(c0, c1));
    }
    
    /**
     * Returns the index of the first appearance of the given target in
     * the given source, starting at the given index, using the given
     * comparator for characters. Returns -1 if the target string is
     * not found.
     * 
     * @param source The source string
     * @param target The target string
     * @param startIndex The start index
     * @param comparator The comparator
     * @return The index
     */
    private static int indexOf(String source, String target, 
        int startIndex, IntBinaryOperator comparator)
    {
        return indexOf(
            source, 0, source.length(), 
            target, 0, target.length(), 
            startIndex, comparator);
    }

    /**
     * Returns the index of the first appearance of the given range of the
     * target in the given range of the source, source, starting at the 
     * given index, using the given comparator for characters. Returns -1 if 
     * the target string is not found.
     * 
     * @param source The source string
     * @param sourceOffset The source offset
     * @param sourceCount The source length
     * @param target The target string
     * @param targetOffset The target offset
     * @param targetCount The target length
     * @param startIndex The start index
     * @param comparator The comparator
     * @return The index
     */
    private static int indexOf(
        String source, int sourceOffset, int sourceCount, 
        String target, int targetOffset, int targetCount,
        int startIndex, IntBinaryOperator comparator)
    {
        int fromIndex = startIndex;
        
        // Adapted from String#indexOf
        if (fromIndex >= sourceCount)
        {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0)
        {
            fromIndex = 0;
        }
        if (targetCount == 0)
        {
            return fromIndex;
        }

        char first = target.charAt(targetOffset);
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++)
        {
            if (comparator.applyAsInt(source.charAt(i), first) != 0)
            {
                while (++i <= max && 
                    comparator.applyAsInt(source.charAt(i), first) != 0)
                    {
                        // Empty 
                    }
            }
            if (i <= max)
            {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end
                    && comparator.applyAsInt(source.charAt(j),
                        target.charAt(k)) == 0; j++, k++)
                {
                    // Empty 
                }
                if (j == end)
                {
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the index of the previous appearance of the given target in
     * the given source, starting at the given index. Returns -1 if the 
     * target string is not found.
     * 
     * @param source The source string
     * @param target The target string
     * @param startIndex The start index
     * @param ignoreCase Whether the case should be ignored
     * @return The index
     */
    static int lastIndexOf(String source, String target, 
        int startIndex, boolean ignoreCase)
    {
        if (ignoreCase)
        {
            return lastIndexOf(source, target, startIndex, IGNORING_CASE);
        }
        return lastIndexOf(source, target, startIndex, 
            (c0, c1) -> Integer.compare(c0, c1));
    }
    
    /**
     * Returns the index of the previous appearance of the given target in
     * the given source, starting at the given index, using the given
     * comparator for characters. Returns -1 if the target string is
     * not found.
     * 
     * @param source The source string
     * @param target The target string
     * @param startIndex The start index
     * @param comparator The comparator
     * @return The index
     */
    private static int lastIndexOf(String source, String target, 
        int startIndex, IntBinaryOperator comparator)
    {
        return lastIndexOf(
            source, 0, source.length(), 
            target, 0, target.length(), 
            startIndex, comparator);
    }
    
    /**
     * Returns the index of the previous appearance of the given range of the
     * target in the given range of the source, source, starting at the 
     * given index, using the given comparator for characters. Returns -1 if 
     * the target string is not found.
     * 
     * @param source The source string
     * @param sourceOffset The source offset
     * @param sourceCount The source length
     * @param target The target string
     * @param targetOffset The target offset
     * @param targetCount The target length
     * @param startIndex The start index
     * @param comparator The comparator
     * @return The index
     */
    static int lastIndexOf(
        String source, int sourceOffset, int sourceCount,
        String target, int targetOffset, int targetCount, 
        int startIndex, IntBinaryOperator comparator)
    {
        int fromIndex = startIndex;
        
        // Adapted from String#lastIndexOf
        int rightIndex = sourceCount - targetCount;
        if (fromIndex < 0)
        {
            return -1;
        }
        if (fromIndex > rightIndex)
        {
            fromIndex = rightIndex;
        }
        if (targetCount == 0)
        {
            return fromIndex;
        }

        int strLastIndex = targetOffset + targetCount - 1;
        char strLastChar = target.charAt(strLastIndex);
        int min = sourceOffset + targetCount - 1;
        int i = min + fromIndex;

        startSearchForLastChar: while (true)
        {
            while (i >= min && 
                comparator.applyAsInt(source.charAt(i), strLastChar) != 0)
            {
                i--;
            }
            if (i < min)
            {
                return -1;
            }
            int j = i - 1;
            int start = j - (targetCount - 1);
            int k = strLastIndex - 1;

            while (j > start)
            {
                if (comparator.applyAsInt(
                        source.charAt(j--), target.charAt(k--)) != 0)
                {
                    i--;
                    continue startSearchForLastChar;
                }
            }
            return start - sourceOffset + 1;
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     */
    private StringUtils()
    {
        // Private constructor to prevent instantiation
    }

}
