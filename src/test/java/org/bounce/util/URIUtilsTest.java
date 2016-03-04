/*
 * $Id$
 *
 * Copyright (c) 2002 - 2008, Edwin Dankert
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, 
 *	 this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright 
 * 	 notice, this list of conditions and the following disclaimer in the 
 *	 documentation and/or other materials provided with the distribution. 
 * * Neither the name of 'Edwin Dankert' nor the names of its contributors 
 *	 may  be used to endorse or promote products derived from this software 
 *	 without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.bounce.util;

import java.io.File;
import java.net.URI;

import junit.framework.TestCase;

import org.bounce.util.URIUtils;

public class URIUtilsTest extends TestCase {

    protected void setUp() {
        // nothing to do
    }

    protected void tearDown() {
        // nothing to do
    }
    
    public void testGetName() {
        URI uri = URIUtils.createURI("\\root\\dir\\build.xml");
        assertEquals("build.xml", URIUtils.getName(uri));

        uri = URIUtils.createURI("\\root\\dir\\");
        assertEquals("", URIUtils.getName(uri));

        uri = URIUtils.createURI("root/dir/");
        assertEquals("", URIUtils.getName(uri));

        uri = null;
        assertEquals("", URIUtils.getName(uri));
    }

    public void testToFile() {
        URI file = URIUtils.createURI("file:/temp/test/test.tst");
        assertEquals(new File("/temp/test/test.tst"), URIUtils.toFile(file));

        file = URIUtils.createURI("/temp/test/test.tst");
        File result = URIUtils.toFile(file);
        assertEquals(new File("/temp/test/test.tst"), result);

        file = URIUtils.createURI("file:test/test.tst");
        result = URIUtils.toFile(file);
        assertEquals(new File((File)null, "test/test.tst"), result);

        file = URIUtils.createURI("http:/temp/test/test.tst");
        assertEquals(new File("/temp/test/test.tst"), URIUtils.toFile(file));
    }

    public void testGetDirectoryName() {
        URI uri = URIUtils.createURI("/dir/");
        assertEquals("dir", URIUtils.getDirectoryName(uri));

        uri = URIUtils.createURI("/dir/file");
        assertEquals("dir", URIUtils.getDirectoryName(uri));

        uri = URIUtils.createURI("dir/file");
        assertEquals("dir", URIUtils.getDirectoryName(uri));

        uri = URIUtils.createURI("/root/dir/");
        assertEquals("dir", URIUtils.getDirectoryName(uri));

        uri = URIUtils.createURI("/root/dir/file");
        assertEquals("dir", URIUtils.getDirectoryName(uri));
    }

    public void testToStringURI() {
        URI uri = URIUtils.createURI("/root");
        assertEquals("/root", URIUtils.toString(uri));

        uri = URIUtils.createURI("file:/root");
        assertEquals("file:/root", URIUtils.toString(uri));
    }

    public void testGetRelativeURI() {
        URI base = URIUtils.createURI( "file:/temp/test/test.tst");
        URI relative = URIUtils.createURI( "file:/temp/test/sub/test.tst");
        URI result = URIUtils.getRelativeURI(base, relative);
        assertEquals("sub/test.tst", URIUtils.toString(result));

        relative = URIUtils.createURI( "file:/test/sub sub/test.tst");
        result = URIUtils.getRelativeURI(base, relative);
        assertEquals("file:/test/sub sub/test.tst", URIUtils.toString(result));

        relative = URIUtils.createURI( "file:/test/sub/test.tst");
        result = URIUtils.getRelativeURI(base, relative);
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.getRelativeURI(null, relative);
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        relative = URIUtils.createURI( "file:/test/sub/test.tst");
        result = URIUtils.getRelativeURI(base, relative);
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        relative = URIUtils.createURI( "http:/www.edankert.com/sub/test.tst");
        result = URIUtils.getRelativeURI(base, relative);
        assertEquals("http:/www.edankert.com/sub/test.tst", URIUtils.toString(result));

        base = URIUtils.createURI( "file:/temp/test test/test.tst");

        relative = URIUtils.createURI( "file:/temp/test test/sub/test.tst");
        result = URIUtils.getRelativeURI(base, relative);
        assertEquals("sub/test.tst", URIUtils.toString(result));

        relative = URIUtils.createURI( "file:/test/sub sub/test.tst");
        result = URIUtils.getRelativeURI(base, relative);
        assertEquals("file:/test/sub sub/test.tst", URIUtils.toString(result));

        relative = URIUtils.createURI( "file:/test/sub/test.tst");
        result = URIUtils.getRelativeURI(base, relative);
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.getRelativeURI(null, relative);
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        relative = URIUtils.createURI( "file:/test/sub/test.tst");
        result = URIUtils.getRelativeURI(base, relative);
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        relative = URIUtils.createURI( "http:/www.edankert.com/sub/test.tst");
        result = URIUtils.getRelativeURI(base, relative);
        assertEquals("http:/www.edankert.com/sub/test.tst", URIUtils.toString(result));
    }

    public void testGetRelativeFile() {
        URI base = new File("/temp/test/test.tst").toURI();

        String result = URIUtils.getRelativePath(base, new File("/temp/test/sub/test.tst"));
        assertEquals("sub/test.tst", result);

        if (File.separatorChar == '\\') {
            result = URIUtils.getRelativePath(base, new File("\\temp\\test\\sub\\test.tst"));
            assertEquals("sub/test.tst", result);
    
            result = URIUtils.getRelativePath(base, new File("\\temp\\sub\\test.tst"));
            assertEquals("../sub/test.tst", result);
        }
        
        result = URIUtils.getRelativePath(base, new File("/temp/sub/test.tst"));
        assertEquals("../sub/test.tst", result);

        result = URIUtils.getRelativePath(base, new File("/test/sub/test.tst"));
        
        URI uri = new File((File)null, "/test/sub/test.tst").toURI();
        assertEquals(uri.toString(), result);

        result = URIUtils.getRelativePath(base, new File("/test/sub sub/test.tst"));

        uri = new File((File)null, "/test/sub sub/test.tst").toURI();

        assertEquals(URIUtils.toString(uri), result);

        base = new File("/temp/test/test.tst").toURI();

        result = URIUtils.getRelativePath(base, new File("/temp/test/sub/test.tst"));
        assertEquals("sub/test.tst", result);

        if (File.separatorChar == '\\') {
            result = URIUtils.getRelativePath(base, new File("\\temp\\test\\sub\\test.tst"));
            assertEquals("sub/test.tst", result);
        }

        result = URIUtils.getRelativePath(base, new File("/test/sub/test.tst"));

        uri = new File((File)null, "/test/sub/test.tst").toURI();
        
        assertEquals(URIUtils.toString(uri), result);

        result = URIUtils.getRelativePath(base, new File("/test/sub sub/test.tst"));
        uri = new File((File)null, "/test/sub sub/test.tst").toURI();

        assertEquals(URIUtils.toString(uri), result);

        result = URIUtils.getRelativePath(base, new File("/test/sub/test.tst"));
        uri = new File((File)null, "/test/sub/test.tst").toURI();
        assertEquals(URIUtils.toString(uri), result);

        base = URIUtils.createURI( "file:/test.tst");
        result = URIUtils.getRelativePath(base, new File("/sub/test.tst"));
        uri = new File((File)null, "/sub/test.tst").toURI();
        assertEquals(URIUtils.toString(uri), result);

        base = URIUtils.createURI( "/test.tst");
        result = URIUtils.getRelativePath(base, new File("/sub/test.tst"));
        assertEquals(URIUtils.toString(uri), result);

        base = URIUtils.createURI( "file:/");
        result = URIUtils.getRelativePath(base, new File("/sub/test.tst"));
        assertEquals(URIUtils.toString(uri), result);

        base = URIUtils.createURI( "file:test.tst");
        result = URIUtils.getRelativePath(base, new File("/sub/test.tst"));
        assertEquals(URIUtils.toString(uri), result);

        Exception exception = null;
        
        try {
            base = URIUtils.createURI( "file:");
        } catch (Exception e) {
            exception = e;
        }
        
        assertNotNull(exception);
    }

//    public void testGetRelativeFileForDir() {
//        URI base = URIUtils.createURI( "file:/temp/test/test.tst");
//
//        String result = URIUtils.getRelativePath(base, "/temp/test/sub/");
//        assertEquals("sub", result);
//
//        result = URIUtils.getRelativePath(base, "\\temp\\test\\sub\\");
//        assertEquals("sub", result);
//
//        result = URIUtils.getRelativePath(base, "/test/sub/");
//        assertEquals("/test/sub/", result);
//
//        result = URIUtils.getRelativePath(base, "/test/sub sub/");
//        assertEquals("/test/sub sub/", result);
//
//        result = URIUtils.getRelativePath(base, "/test/sub/");
//        assertEquals("/test/sub/", result);
//
//        base = URIUtils.createURI( "file:/temp/test test/test.tst");
//
//        result = URIUtils.getRelativePath(base, "/temp/test test/sub/");
//        assertEquals("sub", result);
//
//        result = URIUtils.getRelativePath(base, "\\temp\\test test\\sub\\");
//        assertEquals("sub", result);
//
//        result = URIUtils.getRelativePath(base, "/test/sub/");
//        assertEquals("/test/sub/", result);
//
//        result = URIUtils.getRelativePath(base, "test/sub sub/");
//        assertEquals("test/sub sub/", result);
//
//        result = URIUtils.getRelativePath(base, "/test/sub/");
//        assertEquals("/test/sub/", result);
//        
//        base = URIUtils.createURI( "file:/edwin/dev/xmlhammer/src/test/resource/projects/XPathAttributesElements.xh");
//        result = URIUtils.getRelativePath(base, "\\edwin\\dev\\");
//        assertEquals("../../../../../", result);
//
//        base = URIUtils.createURI( "file:/edwin/dev/xmlhammer/src/test/resource/projects/XPathAttributesElements.xh");
//        result = URIUtils.getRelativePath(base, "\\edwin\\dev\\test.tst");
//        assertEquals("../../../../../test.tst", result);
//
//        base = URIUtils.createURI( "file:/edwin/dev/xmlhammer/src/test/resource/projects/");
//        result = URIUtils.getRelativePath(base, "/edwin/dev/");
//        assertEquals("../../../../../", result);
//
//        base = URIUtils.createURI( "file:/edwin/dev/xmlhammer/src/test/resource/projects/");
//        result = URIUtils.getRelativePath(base, "/edwin/dev/test.tst");
//        assertEquals("../../../../../test.tst", result);
//
//        result = URIUtils.getRelativePath(base, "\\Documents and Settings\\Edwin");
//        assertEquals("/Documents and Settings/Edwin", result);
//    }

    public void testComposeURI() {
        URI base = URIUtils.createURI( "file:/temp/test/test.tst");
        URI result = URIUtils.composeURI(base, "sub/test.tst");
        assertEquals("file:/temp/test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "../../test/sub/test.tst");
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "../../test/sub sub/test.tst");
        assertEquals("file:/test/sub sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(null, "../../test/sub/test.tst");
        assertEquals("../../test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "file:/test/sub/test.tst");
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "file:/test/sub/test.tst");
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "http:/www.edankert.com/sub/test.tst");
        assertEquals("http:/www.edankert.com/sub/test.tst", URIUtils.toString(result));

        base = URIUtils.createURI( "file:/temp/test test/test.tst");

        result = URIUtils.composeURI(base, "sub/test.tst");
        assertEquals("file:/temp/test test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "../../test/sub/test.tst");
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "../../test/sub sub/test.tst");
        assertEquals("file:/test/sub sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(null, "../../test/sub/test.tst");
        assertEquals("../../test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "file:/test/sub/test.tst");
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "file:/test/sub/test.tst");
        assertEquals("file:/test/sub/test.tst", URIUtils.toString(result));

        result = URIUtils.composeURI(base, "http:/www.edankert.com/sub/test.tst");
        assertEquals("http:/www.edankert.com/sub/test.tst", URIUtils.toString(result));
}

    public void testComposeFile() {
        URI base = URIUtils.createURI( "file:/temp/test/test.tst");
        String result = URIUtils.composePath(base, "sub/test.tst");
        assertEquals(File.separator+"temp"+File.separator+"test"+File.separator+"sub"+File.separator+"test.tst", result);

        result = URIUtils.composePath(base, "../../test/sub/test.tst");
        assertEquals(File.separator+"test"+File.separator+"sub"+File.separator+"test.tst", result);

        result = URIUtils.composePath(base, "../../test/sub sub/test.tst");
        assertEquals(File.separator+"test"+File.separator+"sub sub"+File.separator+"test.tst", result);

        result = URIUtils.composePath(null, "../../test/sub/test.tst");
        assertEquals("../../test/sub/test.tst", result);

        result = URIUtils.composePath(base, "file:/test/sub/test.tst");
        assertEquals(File.separator+"test"+File.separator+"sub"+File.separator+"test.tst", result);

//        result = URIUtils.composeFile(base, "file:/test/sub/test.tst");
//        assertEquals("D:"+File.separator+"test"+File.separator+"sub"+File.separator+"test.tst", result);

        boolean exception = false;
        try {
            result = URIUtils.composePath(base, "http:/www.edankert.com/sub/test.tst");
        } catch (IllegalArgumentException e) {
            exception = true;
        }

        assertTrue("No Illegal Argument Exception has been thrown.", exception);

        base = URIUtils.createURI( "file:/temp/test test/test.tst");

        result = URIUtils.composePath(base, "sub/test.tst");
        assertEquals(File.separator+"temp"+File.separator+"test test"+File.separator+"sub"+File.separator+"test.tst", result);

        result = URIUtils.composePath(base, "../../test/sub/test.tst");
        assertEquals(File.separator+"test"+File.separator+"sub"+File.separator+"test.tst", result);

        result = URIUtils.composePath(base, "../../test/sub sub/test.tst");
        assertEquals(File.separator+"test"+File.separator+"sub sub"+File.separator+"test.tst", result);

        result = URIUtils.composePath(null, "../../test/sub/test.tst");
        assertEquals("../../test/sub/test.tst", result);

        result = URIUtils.composePath(base, "file:/test/sub/test.tst");
        assertEquals(File.separator+"test"+File.separator+"sub"+File.separator+"test.tst", result);

        result = URIUtils.composePath(base, "file:/test/sub/test.tst");
        assertEquals(File.separator+"test"+File.separator+"sub"+File.separator+"test.tst", result);

        base = URIUtils.createURI( "file:/edwin/dev/xmlhammer/src/test/resource/projects/XPathAttributesElements.xh");
        result = URIUtils.composePath(base, "../../../../../../../Documents and Settings/Edwin");
        assertEquals(File.separator+"Documents and Settings"+File.separator+"Edwin", result);
    }
}
