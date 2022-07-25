import classes.gui.*;
import classes.*;
import client.NetworkClient;
import server.ClientThread;
import server.Database;
import server.ServerController;
import server.errors.DuplicateUsernameException;
import server.errors.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * A framework to run public test cases.
 * <p>
 * LocalTests
 *
 * @author Ethan Loev
 * @version November 22, 2020
 */

public class LocalTests {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCases.class);
        if (result.wasSuccessful()) {
            System.out.println("Excellent - Test ran successfully");
        } else {
            for (Failure failure : result.getFailures()) {
                System.out.println(failure.toString());
            }
        }
    }

    /**
     * A set of public test cases.
     * <p>
     * TestCases
     *
     * @author Ethan Loev
     * @version November 22, 2020
     */

    public static class TestCases {
        private final PrintStream originalOutput = System.out;
        private final InputStream originalSysin = System.in;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayInputStream testIn;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayOutputStream testOut;

        @Before
        public void outputStart() {
            testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }

        @After
        public void restoreInputAndOutput() {
            System.setIn(originalSysin);
            System.setOut(originalOutput);
        }

        private String getOutput() {
            return testOut.toString();
        }

        @SuppressWarnings("SameParameterValue")
        private void receiveInput(String str) {
            testIn = new ByteArrayInputStream(str.getBytes());
            System.setIn(testIn);
        }

        @Test(timeout = 1000)
        public void networkClientClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = NetworkClient.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `NetworkClient` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `NetworkClient` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `NetworkClient` extends `Object`!", Object.class, superclass);
            Assert.assertEquals("Ensure that `NetworkClient` implements no interfaces!", 0, superinterfaces.length);
        } //networkClientClassDeclarationTest

        @Test(timeout = 1_000)
        public void networkClientParameterizedConstructorDeclarationTest() {

            Class<?> clazz = NetworkClient.class;
            String className = "NetworkClient";

            Constructor<?> constructor;
            int modifiers;

            try {
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares both constructors as `public` and they have " +
                        "three parameters with type String, String, and String, and the other with an additional " +
                        "HashMap!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
        } //networkClientParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void networkClientFieldDeclarationTestOne() {

            Class<?> clazz = NetworkClient.class;
            String className = "NetworkClient";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field2 = "socket";
            fields.add(field2);
            String field3 = "currentUser";
            fields.add(field3);
            String field4 = "reader";
            fields.add(field4);
            String field5 = "writer";
            fields.add(field5);
            String field6 = "receiveServerInput";
            fields.add(field6);
            String field7 = "currentObject";
            fields.add(field7);
            String field8 = "awaitingResponse";
            fields.add(field8);
            String field9 = "error";
            fields.add(field9);
            String field10 = "errormsg";
            fields.add(field10);
            String field11 = "requestedName";
            fields.add(field11);
            String field12 = "requestedAge";
            fields.add(field12);
            String field13 = "closeGUI";
            fields.add(field13);
            String field14 = "newChat";
            fields.add(field14);

            Class<?> class2 = Socket.class;
            types.add(class2);
            Class<?> class3 = User.class;
            types.add(class3);
            Class<?> class4 = BufferedReader.class;
            types.add(class4);
            Class<?> class5 = PrintWriter.class;
            types.add(class5);
            Class<?> class6 = AtomicBoolean.class;
            types.add(class6);
            Class<?> class7 = String.class;
            types.add(class7);
            Class<?> class8 = boolean.class;
            types.add(class8);
            Class<?> class9 = boolean.class;
            types.add(class9);
            Class<?> class10 = String.class;
            types.add(class10);
            Class<?> class11 = String.class;
            types.add(class11);
            Class<?> class12 = String.class;
            types.add(class12);
            Class<?> class13 = boolean.class;
            types.add(class13);
            Class<?> class14 = boolean.class;
            types.add(class14);

            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `public`!",
                        Modifier.isPublic(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);
            }
        } //networkClientDeclarationTesting

        @Test(timeout = 1000)
        public void networkClientMethodDeclarationTesting() {

            Class<?> clazz = NetworkClient.class;
            String className = "NetworkClient";

            Method testMethod;
            int modifiers;
            Class<?> type;

            ArrayList<String> methodNames = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();
            ArrayList<Class<?>[]> inputClasses = new ArrayList<>();

            String method1 = "deserializeUser";
            methodNames.add(method1);
            String method2 = "signUp";
            methodNames.add(method2);
            String method3 = "setSocket";
            methodNames.add(method3);
            String method4 = "setWriter";
            methodNames.add(method4);
            String method5 = "setReader";
            methodNames.add(method5);
            String method6 = "setCurrentUser";
            methodNames.add(method6);
            String method7 = "getSocket";
            methodNames.add(method7);
            String method10 = "determineObject";
            methodNames.add(method10);
            String method11 = "sendMessage";
            methodNames.add(method11);
            String method12 = "editMessage";
            methodNames.add(method12);
            String method13 = "createDm";
            methodNames.add(method13);
            String method14 = "createGroup";
            methodNames.add(method14);
            String method15 = "addUser";
            methodNames.add(method15);
            String method16 = "removeUser";
            methodNames.add(method16);
            String method17 = "removeChat";
            methodNames.add(method17);
            String method18 = "requestInfo";
            methodNames.add(method18);
            String method19 = "deleteAccount";
            methodNames.add(method19);
            String method20 = "signOut";
            methodNames.add(method20);
            String method21 = "login";
            methodNames.add(method21);
            String method22 = "setRequestedName";
            methodNames.add(method22);
            String method23 = "setRequestedAge";
            methodNames.add(method23);
            String method24 = "removeMessage";
            methodNames.add(method24);
            String method25 = "editName";
            methodNames.add(method25);
            String method26 = "editAge";
            methodNames.add(method26);
            String method27 = "editPassword";
            methodNames.add(method27);

            Class<?>[] input1 = new Class[1];
            input1[0] = String.class;
            inputClasses.add(input1);
            Class<?>[] input2 = new Class[4];
            input2[0] = String.class;
            input2[1] = String.class;
            input2[2] = int.class;
            input2[3] = String.class;
            inputClasses.add(input2);
            Class<?>[] input3 = new Class[1];
            input3[0] = Socket.class;
            inputClasses.add(input3);
            Class<?>[] input4 = new Class[1];
            input4[0] = PrintWriter.class;
            inputClasses.add(input4);
            Class<?>[] input5 = new Class[1];
            input5[0] = BufferedReader.class;
            inputClasses.add(input5);
            Class<?>[] input6 = new Class[1];
            input6[0] = User.class;
            inputClasses.add(input6);
            inputClasses.add(null);
            Class<?>[] input10 = new Class[1];
            input10[0] = String.class;
            inputClasses.add(input10);
            Class<?>[] input11 = new Class[1];
            input11[0] = String.class;
            inputClasses.add(input11);
            Class<?>[] input12 = new Class[3];
            input12[0] = String.class;
            input12[1] = String.class;
            input12[2] = String.class;
            inputClasses.add(input12);
            Class<?>[] input13 = new Class[1];
            input13[0] = String.class;
            inputClasses.add(input13);
            Class<?>[] input14 = new Class[1];
            input14[0] = String.class;
            inputClasses.add(input14);
            Class<?>[] input15 = new Class[1];
            input15[0] = String.class;
            inputClasses.add(input15);
            Class<?>[] input16 = new Class[1];
            input16[0] = String.class;
            inputClasses.add(input16);
            Class<?>[] input17 = new Class[1];
            input17[0] = UUID.class;
            inputClasses.add(input17);
            Class<?>[] input18 = new Class[1];
            input18[0] = String.class;
            inputClasses.add(input18);
            inputClasses.add(null);
            inputClasses.add(null);
            Class<?>[] input21 = new Class[2];
            input21[0] = String.class;
            input21[1] = String.class;
            inputClasses.add(input21);
            Class<?>[] input22 = new Class[1];
            input22[0] = String.class;
            inputClasses.add(input22);
            Class<?>[] input23 = new Class[1];
            input23[0] = String.class;
            inputClasses.add(input23);
            Class<?>[] input24 = new Class[2];
            input24[0] = String.class;
            input24[1] = String.class;
            inputClasses.add(input24);
            Class<?>[] input25 = new Class[1];
            input25[0] = String.class;
            inputClasses.add(input25);
            Class<?>[] input26 = new Class[1];
            input26[0] = String.class;
            inputClasses.add(input26);
            Class<?>[] input27 = new Class[2];
            input27[0] = String.class;
            input27[1] = String.class;
            inputClasses.add(input27);


            Class<?> class1 = User.class;
            types.add(class1);
            Class<?> class2 = void.class;
            types.add(class2);
            Class<?> class3 = void.class;
            types.add(class3);
            Class<?> class4 = void.class;
            types.add(class4);
            Class<?> class5 = void.class;
            types.add(class5);
            Class<?> class6 = void.class;
            types.add(class6);
            Class<?> class7 = Socket.class;
            types.add(class7);
            Class<?> class10 = String.class;
            types.add(class10);
            Class<?> class11 = void.class;
            types.add(class11);
            Class<?> class12 = void.class;
            types.add(class12);
            Class<?> class13 = void.class;
            types.add(class13);
            Class<?> class14 = void.class;
            types.add(class14);
            Class<?> class15 = void.class;
            types.add(class15);
            Class<?> class16 = void.class;
            types.add(class16);
            Class<?> class17 = void.class;
            types.add(class17);
            Class<?> class18 = void.class;
            types.add(class18);
            Class<?> class19 = void.class;
            types.add(class19);
            Class<?> class20 = void.class;
            types.add(class20);
            Class<?> class21 = void.class;
            types.add(class21);
            Class<?> class22 = void.class;
            types.add(class22);
            Class<?> class23 = void.class;
            types.add(class23);
            Class<?> class24 = void.class;
            types.add(class24);
            Class<?> class25 = void.class;
            types.add(class25);
            Class<?> class26 = void.class;
            types.add(class26);
            Class<?> class27 = void.class;
            types.add(class27);

            for (int i = 0; i < methodNames.size(); i++) {
                try {
                    testMethod = clazz.getDeclaredMethod(methodNames.get(i), inputClasses.get(i));
                } catch (NoSuchMethodException e) {
                    Assert.fail("Ensure that `" + className + "` declares a method named `" + methodNames.get(i) +
                            "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testMethod.getModifiers();

                type = testMethod.getReturnType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                        "`public`!", Modifier.isPublic(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is the " +
                        "correct type!", expectedType, type);
            }
        } //networkClientMethodDeclarationTesting

        @Test(timeout = 1000)
        public void userClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = User.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `User` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `User` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `User` extends `Object`!", Object.class, superclass);
            Assert.assertEquals("Ensure that `User` implements Serializable!", 1, superinterfaces.length);
        } //userClassDeclarationTest

        @Test(timeout = 1_000)
        public void userParameterizedConstructorDeclarationTest() {

            Class<?> clazz = User.class;
            String className = "User";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            int modifiers;
            int modifiers2;

            try {
                constructor2 = clazz.getDeclaredConstructor(String.class, String.class, int.class, String.class,
                        HashMap.class);
                constructor = clazz.getDeclaredConstructor(String.class, String.class, int.class, String.class);
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares both constructors as `public` and they have " +
                        "three parameters with type String, String, and String, and the other with an additional " +
                        "HashMap!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
        } //userParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void userFieldDeclarationTestOne() {

            Class<?> clazz = User.class;
            String className = "User";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "username";
            fields.add(field1);
            String field2 = "name";
            fields.add(field2);
            String field3 = "password";
            fields.add(field3);
            String field4 = "chats";
            fields.add(field4);
            String field5 = "age";
            fields.add(field5);

            Class<?> class1 = String.class;
            types.add(class1);
            Class<?> class2 = String.class;
            types.add(class2);
            Class<?> class3 = String.class;
            types.add(class3);
            Class<?> class4 = HashMap.class;
            types.add(class4);
            Class<?> class5 = int.class;
            types.add(class5);

            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);
            }
        } //userFieldDeclarationTesting

        @Test(timeout = 1000)
        public void userMethodDeclarationTesting() {

            Class<?> clazz = User.class;
            String className = "User";

            Method testMethod;
            int modifiers;
            Class<?> type;

            ArrayList<String> methodNames = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();
            ArrayList<Class<?>[]> inputClasses = new ArrayList<>();

            String method1 = "getUsername";
            methodNames.add(method1);
            String method2 = "getName";
            methodNames.add(method2);
            String method3 = "setName";
            methodNames.add(method3);
            String method4 = "getPassword";
            methodNames.add(method4);
            String method5 = "setPassword";
            methodNames.add(method5);
            String method6 = "getChats";
            methodNames.add(method6);
            String method7 = "addChat";
            methodNames.add(method7);
            String method8 = "removeChat";
            methodNames.add(method8);
            String method9 = "getAge";
            methodNames.add(method9);
            String method10 = "setAge";
            methodNames.add(method10);

            inputClasses.add(null);
            inputClasses.add(null);
            Class<?>[] input3 = new Class[1];
            input3[0] = String.class;
            inputClasses.add(input3);
            inputClasses.add(null);
            Class<?>[] input5 = new Class[1];
            input5[0] = String.class;
            inputClasses.add(input5);
            inputClasses.add(null);
            Class<?>[] input7 = new Class[1];
            input7[0] = Chat.class;
            inputClasses.add(input7);
            Class<?>[] input8 = new Class[1];
            input8[0] = UUID.class;
            inputClasses.add(input8);
            inputClasses.add(null);
            Class<?>[] input10 = new Class[1];
            input10[0] = int.class;
            inputClasses.add(input10);


            Class<?> class1 = String.class;
            types.add(class1);
            Class<?> class2 = String.class;
            types.add(class2);
            Class<?> class3 = void.class;
            types.add(class3);
            Class<?> class4 = String.class;
            types.add(class4);
            Class<?> class5 = void.class;
            types.add(class5);
            Class<?> class6 = HashMap.class;
            types.add(class6);
            Class<?> class7 = void.class;
            types.add(class7);
            Class<?> class8 = void.class;
            types.add(class8);
            Class<?> class9 = int.class;
            types.add(class9);
            Class<?> class10 = void.class;
            types.add(class10);

            for (int i = 0; i < methodNames.size(); i++) {
                try {
                    testMethod = clazz.getDeclaredMethod(methodNames.get(i), inputClasses.get(i));
                } catch (NoSuchMethodException e) {
                    Assert.fail("Ensure that `" + className + "` declares a method named `" + methodNames.get(i) +
                            "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testMethod.getModifiers();

                type = testMethod.getReturnType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                        "`public`!", Modifier.isPublic(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is the " +
                        "correct type!", expectedType, type);
            }
        } //userMethodDeclarationTesting

        @Test(timeout = 1000)
        public void messageClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = Message.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `Message` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `Message` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `Message` extends `Object`!", Object.class, superclass);
            Assert.assertEquals("Ensure that `Message` implements Serializable!", 1, superinterfaces.length);
        } //messageClassDeclarationTest

        @Test(timeout = 1_000)
        public void messageParameterizedConstructorDeclarationTest() {

            Class<?> clazz = Message.class;
            String className = "Message";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            Constructor<?> constructor3;
            int modifiers;
            int modifiers2;
            int modifiers3;

            try {
                constructor3 = clazz.getDeclaredConstructor(String.class, String.class);
                constructor2 = clazz.getDeclaredConstructor(String.class, String.class, LocalDateTime.class,
                        UUID.class);
                constructor = clazz.getDeclaredConstructor(String.class, String.class, LocalDateTime.class);
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();
            modifiers3 = constructor3.getModifiers();


            Assert.assertTrue("Ensure that all `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that all `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
            Assert.assertTrue("Ensure that all `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers3));
        } //messageParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void messageFieldDeclarationTestOne() {

            Class<?> clazz = Message.class;
            String className = "Message";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "id";
            fields.add(field1);
            String field2 = "senderUsername";
            fields.add(field2);
            String field3 = "message";
            fields.add(field3);
            String field4 = "timestamp";
            fields.add(field4);

            Class<?> class1 = UUID.class;
            types.add(class1);
            Class<?> class2 = String.class;
            types.add(class2);
            Class<?> class3 = String.class;
            types.add(class3);
            Class<?> class4 = LocalDateTime.class;
            types.add(class4);

            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);
            }
        } //messageFieldDeclarationTesting

        @Test(timeout = 1000)
        public void messageMethodDeclarationTesting() {

            Class<?> clazz = Message.class;
            String className = "Message";

            Method testMethod;
            int modifiers;
            Class<?> type;

            ArrayList<String> methodNames = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();
            ArrayList<Class<?>[]> inputClasses = new ArrayList<>();

            String method1 = "getId";
            methodNames.add(method1);
            String method2 = "getMessage";
            methodNames.add(method2);
            String method3 = "editMessage";
            methodNames.add(method3);
            String method4 = "getSenderUsername";
            methodNames.add(method4);
            String method5 = "getTimeStamp";
            methodNames.add(method5);

            inputClasses.add(null);
            inputClasses.add(null);
            Class<?>[] input3 = new Class[1];
            input3[0] = String.class;
            inputClasses.add(input3);
            inputClasses.add(null);
            inputClasses.add(null);


            Class<?> class1 = UUID.class;
            types.add(class1);
            Class<?> class2 = String.class;
            types.add(class2);
            Class<?> class3 = void.class;
            types.add(class3);
            Class<?> class4 = String.class;
            types.add(class4);
            Class<?> class5 = LocalDateTime.class;
            types.add(class5);


            for (int i = 0; i < methodNames.size(); i++) {
                try {
                    testMethod = clazz.getDeclaredMethod(methodNames.get(i), inputClasses.get(i));
                } catch (NoSuchMethodException e) {
                    Assert.fail("Ensure that `" + className + "` declares a method named `" + methodNames.get(i) +
                            "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testMethod.getModifiers();

                type = testMethod.getReturnType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                        "`public`!", Modifier.isPublic(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is the " +
                        "correct type!", expectedType, type);
            }
        } //messageMethodDeclarationTesting

        @Test(timeout = 1000)
        public void groupClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = Group.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `Group` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `Group` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `Group` extends `DirectMessage`!", DirectMessage.class, superclass);
            Assert.assertEquals("Ensure that `Group` implements no interfaces!", 0, superinterfaces.length);
        } //groupClassDeclarationTest

        @Test(timeout = 1_000)
        public void groupParameterizedConstructorDeclarationTest() {

            Class<?> clazz = Group.class;
            String className = "Group";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            int modifiers;
            int modifiers2;

            try {
                constructor2 = clazz.getDeclaredConstructor(String.class, LinkedList.class, HashMap.class, UUID.class);
                constructor = clazz.getDeclaredConstructor(String.class, LinkedList.class, HashMap.class);
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
        } //groupParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void groupFieldDeclarationTestOne() {

            Class<?> clazz = Group.class;
            String className = "Group";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "serialVersionUID";
            fields.add(field1);
            String field2 = "groupName";
            fields.add(field2);

            Class<?> class1 = long.class;
            types.add(class1);
            Class<?> class2 = String.class;
            types.add(class2);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);
            }
        } //groupFieldDeclarationTesting

        @Test(timeout = 1000)
        public void groupMethodDeclarationTesting() {

            Class<?> clazz = Group.class;
            String className = "Group";

            Method testMethod;
            int modifiers;
            Class<?> type;

            ArrayList<String> methodNames = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();
            ArrayList<Class<?>[]> inputClasses = new ArrayList<>();

            String method1 = "addParticipant";
            methodNames.add(method1);
            String method2 = "removeParticipant";
            methodNames.add(method2);
            String method3 = "setGroupName";
            methodNames.add(method3);
            String method4 = "getGroupName";
            methodNames.add(method4);

            Class<?>[] input1 = new Class[1];
            input1[0] = String.class;
            inputClasses.add(input1);
            Class<?>[] input2 = new Class[1];
            input2[0] = String.class;
            inputClasses.add(input2);
            Class<?>[] input3 = new Class[1];
            input3[0] = String.class;
            inputClasses.add(input3);
            inputClasses.add(null);


            Class<?> class1 = void.class;
            types.add(class1);
            Class<?> class2 = void.class;
            types.add(class2);
            Class<?> class3 = void.class;
            types.add(class3);
            Class<?> class4 = String.class;
            types.add(class4);


            for (int i = 0; i < methodNames.size(); i++) {
                try {
                    testMethod = clazz.getDeclaredMethod(methodNames.get(i), inputClasses.get(i));
                } catch (NoSuchMethodException e) {
                    Assert.fail("Ensure that `" + className + "` declares a method named `" + methodNames.get(i) +
                            "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testMethod.getModifiers();

                type = testMethod.getReturnType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                        "`public`!", Modifier.isPublic(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is the " +
                        "correct type!", expectedType, type);
            }
        } //groupMethodDeclarationTesting

        @Test(timeout = 1000)
        public void directMessageClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = DirectMessage.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `DirectMessage` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `DirectMessage` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `DirectMessage` extends `Object`!", Object.class, superclass);
            Assert.assertEquals("Ensure that `DirectMessage` implements 'Serializable' and 'Chat'!", 2,
                    superinterfaces.length);
        } //directMessageClassDeclarationTest

        @Test(timeout = 1_000)
        public void directMessageParameterizedConstructorDeclarationTest() {

            Class<?> clazz = DirectMessage.class;
            String className = "DirectMessage";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            Constructor<?> constructor3;
            int modifiers;
            int modifiers2;
            int modifiers3;

            try {
                constructor3 = clazz.getDeclaredConstructor(UUID.class, LinkedList.class, HashMap.class);
                constructor2 = clazz.getDeclaredConstructor(LinkedList.class, HashMap.class);
                constructor = clazz.getDeclaredConstructor(LinkedList.class);
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();
            modifiers3 = constructor3.getModifiers();


            Assert.assertTrue("Ensure that all `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that all `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
            Assert.assertTrue("Ensure that all `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers3));
        } //directMessageParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void directMessageFieldDeclarationTestOne() {

            Class<?> clazz = DirectMessage.class;
            String className = "DirectMessage";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "serialVersionUID";
            fields.add(field1);
            String field2 = "id";
            fields.add(field2);
            String field3 = "usernames";
            fields.add(field3);
            String field4 = "messages";
            fields.add(field4);

            Class<?> class1 = long.class;
            types.add(class1);
            Class<?> class2 = UUID.class;
            types.add(class2);
            Class<?> class3 = LinkedList.class;
            types.add(class3);
            Class<?> class4 = HashMap.class;
            types.add(class4);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                if (!expectedType.equals(LinkedList.class)) {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!"
                            , Modifier.isPrivate(modifiers));

                } else {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is " +
                            "`protected`!", Modifier.isProtected(modifiers));

                }
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //directMessageFieldDeclarationTesting

        @Test(timeout = 1000)
        public void networkClientTestFunc() {

            String className = "NetworkClient";

            NetworkClient networkClient = new NetworkClient();
            networkClient.setSocket(new Socket());
            Socket socket = networkClient.getSocket();
            networkClient.setCurrentUser(new User("1", "2", 3, "4"));
            networkClient.setRequestedAge("18");
            networkClient.setRequestedName("Test");

            Assert.assertNotNull("Ensure that `" + className + "`'s `" + "setSocket" + "` method returns the correct " +
                    "value!", networkClient.getSocket());
            Assert.assertEquals("Ensure that `" + className + "`'s `" + "getSocket" + "` method returns the correct " +
                    "value!", socket, networkClient.getSocket());
            Assert.assertEquals("Ensure that `" + className + "`'s `" + "setRequestedName" + "` sets the correct " +
                    "value!", networkClient.requestedName, "Test");
            Assert.assertEquals("Ensure that `" + className + "`'s `" + "setRequestedAge" + "` method sets the " +
                    "correct value!", networkClient.requestedAge, "18");
            Assert.assertNotNull("Ensure that `" + className + "`'s `" + "setCurrentUser" + "` sets the correct " +
                    "value!", networkClient.currentUser);
        } //networkClientTestFunc

        @Test(timeout = 1000)
        public void directMessageMethodDeclarationTesting() {

            Class<?> clazz = DirectMessage.class;
            String className = "DirectMessage";

            Method testMethod;
            int modifiers;
            Class<?> type;

            ArrayList<String> methodNames = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();
            ArrayList<Class<?>[]> inputClasses = new ArrayList<>();

            String method1 = "getId";
            methodNames.add(method1);
            String method2 = "getParticipants";
            methodNames.add(method2);
            String method3 = "getMessages";
            methodNames.add(method3);
            String method4 = "addMessage";
            methodNames.add(method4);
            String method5 = "editMessage";
            methodNames.add(method5);
            String method6 = "removeMesage";
            methodNames.add(method6);

            inputClasses.add(null);
            inputClasses.add(null);
            inputClasses.add(null);
            Class<?>[] input4 = new Class[1];
            input4[0] = Message.class;
            inputClasses.add(input4);
            Class<?>[] input5 = new Class[2];
            input5[0] = UUID.class;
            input5[1] = String.class;
            inputClasses.add(input5);
            Class<?>[] input6 = new Class[1];
            input6[0] = UUID.class;
            inputClasses.add(input6);


            Class<?> class1 = UUID.class;
            types.add(class1);
            Class<?> class2 = LinkedList.class;
            types.add(class2);
            Class<?> class3 = HashMap.class;
            types.add(class3);
            Class<?> class4 = Message.class;
            types.add(class4);
            Class<?> class5 = Message.class;
            types.add(class5);
            Class<?> class6 = Message.class;
            types.add(class6);


            for (int i = 0; i < methodNames.size(); i++) {
                try {
                    testMethod = clazz.getDeclaredMethod(methodNames.get(i), inputClasses.get(i));
                } catch (NoSuchMethodException e) {
                    Assert.fail("Ensure that `" + className + "` declares a method named `" + methodNames.get(i) +
                            "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testMethod.getModifiers();

                type = testMethod.getReturnType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                        "`public`!", Modifier.isPublic(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is the " +
                        "correct type!", expectedType, type);
            }
        } //directMessageMethodDeclarationTesting

        @Test(timeout = 1_000)
        public void chatClassDeclarationTest() {
            int modifiers;
            Class<?>[] superinterfaces;

            Class<?> clazz = Chat.class;
            String className = "Chat"; // Chat.java has neither fields nor constructors


            // Perform tests

            modifiers = clazz.getModifiers();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `" + className + "` is `public`!", Modifier.isPublic(modifiers));

            Assert.assertTrue("Ensure that `" + className + "` is an `interface`!", Modifier.isInterface(modifiers));

            Assert.assertEquals("Ensure that `" + className + "` implements 0 interface!", 0, superinterfaces.length);
        } //chatClassDeclarationTest

        @Test(timeout = 1000)
        public void directMessageTestFunc() {
            DirectMessage dm = new DirectMessage(new LinkedList<>(), new HashMap<>());
            UUID uuid = dm.getId();
            HashMap<UUID, Message> hashMap = dm.getMessages();
            LinkedList<String> users = dm.getParticipants();
            String className = "DirectMessage";

            Assert.assertNotNull("Ensure that `" + className + "`'s `" + "getId" + "` method returns the correct " +
                    "value!", uuid);
            Assert.assertNotNull("Ensure that `" + className + "`'s `" + "getParticipants" + "` method returns the " +
                    "correct value!", users);
            Assert.assertNotNull("Ensure that `" + className + "`'s `" + "getMessages" + "` method returns the " +
                    "correct value!", hashMap);
        } //directMessageTestFunc

        @Test(timeout = 1000)
        public void dupUsernameMessageClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = DuplicateUsernameException.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `DuplicateUsernameException` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `DuplicateUsernameException` is NOT `abstract`!",
                    Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `DuplicateUsernameException` extends `Exception`!", Exception.class,
                    superclass);
            Assert.assertEquals("Ensure that `DuplicateUsernameException` implements no interfaces!", 0,
                    superinterfaces.length);
        } //dupUsernameMessageClassDeclarationTest

        @Test(timeout = 1_000)
        public void dupUsernameParameterizedConstructorDeclarationTest() {

            Class<?> clazz = DuplicateUsernameException.class;
            String className = "DuplicateUsernameException";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            int modifiers;
            int modifiers2;

            try {
                constructor2 = clazz.getDeclaredConstructor(String.class);
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
        } //dupUsernameParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void dupUsernameFieldDeclarationTestOne() {

            Class<?> clazz = DuplicateUsernameException.class;
            String className = "DuplicateUsernameException";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "serialVersionUID";
            fields.add(field1);


            Class<?> class1 = long.class;
            types.add(class1);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //dupUsernameFieldDeclarationTesting

        @Test(timeout = 1000)
        public void invalidLoginMessageClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = InvalidLoginException.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `InvalidLoginException` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `InvalidLoginException` is NOT `abstract`!",
                    Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `InvalidLoginException` extends `Exception`!", Exception.class,
                    superclass);
            Assert.assertEquals("Ensure that `InvalidLoginException` implements no interfaces!", 0,
                    superinterfaces.length);
        } //invalidLoginMessageClassDeclarationTest

        @Test(timeout = 1_000)
        public void invalidLoginParameterizedConstructorDeclarationTest() {

            Class<?> clazz = InvalidLoginException.class;
            String className = "InvalidLoginException";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            int modifiers;
            int modifiers2;

            try {
                constructor2 = clazz.getDeclaredConstructor(String.class);
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
        } //invalidLoginParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void invalidLoginFieldDeclarationTestOne() {

            Class<?> clazz = InvalidLoginException.class;
            String className = "InvalidLoginException";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "serialVersionUID";
            fields.add(field1);


            Class<?> class1 = long.class;
            types.add(class1);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //invalidLoginFieldDeclarationTesting

        @Test(timeout = 1000)
        public void notFoundClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = NotFoundException.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `NotFoundException` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `NotFoundException` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `NotFoundException` extends `Exception`!", Exception.class, superclass);
            Assert.assertEquals("Ensure that `NotFoundException` implements no interfaces!", 0, superinterfaces.length);
        } //notFoundClassDeclarationTest

        @Test(timeout = 1_000)
        public void notFoundParameterizedConstructorDeclarationTest() {

            Class<?> clazz = NotFoundException.class;
            String className = "NotFoundException";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            int modifiers;
            int modifiers2;

            try {
                constructor2 = clazz.getDeclaredConstructor(String.class);
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
        } //notFoundParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void notFoundFieldDeclarationTestOne() {

            Class<?> clazz = NotFoundException.class;
            String className = "NotFoundException";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "serialVersionUID";
            fields.add(field1);


            Class<?> class1 = long.class;
            types.add(class1);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //notFoundFieldDeclarationTesting

        @Test(timeout = 1000)
        public void logInClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = LogIn.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `LogIn` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `LogIn` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `LogIn` extends `JComponent`!", JComponent.class, superclass);
            Assert.assertEquals("Ensure that `LogIn` implements Runnable!", 1, superinterfaces.length);
        } //logInClassDeclarationTest

        @Test(timeout = 1_000)
        public void logInParameterizedConstructorDeclarationTest() {

            Class<?> clazz = LogIn.class;
            String className = "LogIn";

            Constructor<?> constructor;
            int modifiers;

            try {
                constructor = clazz.getDeclaredConstructor(NetworkClient.class);
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
        } //logInParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void logInFieldDeclarationTestOne() {

            Class<?> clazz = LogIn.class;
            String className = "LogIn";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "logIn";
            fields.add(field1);
            String field2 = "usernameLabel";
            fields.add(field2);
            String field3 = "username";
            fields.add(field3);
            String field4 = "passwordLabel";
            fields.add(field4);
            String field5 = "password";
            fields.add(field5);
            String field6 = "loginButton";
            fields.add(field6);
            String field7 = "mainMenu";
            fields.add(field7);
            String field8 = "frame";
            fields.add(field8);
            String field9 = "nc";
            fields.add(field9);


            Class<?> class1 = LogIn.class;
            types.add(class1);
            Class<?> class2 = JLabel.class;
            types.add(class2);
            Class<?> class3 = JTextField.class;
            types.add(class3);
            Class<?> class4 = JLabel.class;
            types.add(class4);
            Class<?> class5 = JTextField.class;
            types.add(class5);
            Class<?> class6 = JButton.class;
            types.add(class6);
            Class<?> class7 = JButton.class;
            types.add(class7);
            Class<?> class8 = JFrame.class;
            types.add(class8);
            Class<?> class9 = NetworkClient.class;
            types.add(class9);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                if (fields.get(i).equals("frame")) {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `public`!",
                            Modifier.isPublic(modifiers));

                } else {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!"
                            , Modifier.isPrivate(modifiers));

                }

                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //logInFieldDeclarationTesting

        @Test(timeout = 1000)
        public void createAccountClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = CreateAccount.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `CreateAccount` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `CreateAccount` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `CreateAccount` extends `JComponent`!", JComponent.class, superclass);
            Assert.assertEquals("Ensure that `CreateAccount` implements Runnable!", 1, superinterfaces.length);
        } //createAccountClassDeclarationTest

        @Test(timeout = 1_000)
        public void createAccountParameterizedConstructorDeclarationTest() {

            Class<?> clazz = CreateAccount.class;
            String className = "CreateAccount";

            Constructor<?> constructor;
            int modifiers;

            try {
                constructor = clazz.getDeclaredConstructor(NetworkClient.class);
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
        } //createAccountParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void createAccountFieldDeclarationTestOne() {

            Class<?> clazz = CreateAccount.class;
            String className = "CreateAccount";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "account";
            fields.add(field1);
            String field2 = "usernameLabel";
            fields.add(field2);
            String field3 = "username";
            fields.add(field3);
            String field4 = "passwordLabel";
            fields.add(field4);
            String field5 = "password";
            fields.add(field5);
            String field8 = "ageLabel";
            fields.add(field8);
            String field9 = "create";
            fields.add(field9);
            String field10 = "frame";
            fields.add(field10);
            String field11 = "nameLabel";
            fields.add(field11);
            String field12 = "name";
            fields.add(field12);
            String field13 = "ageGet";
            fields.add(field13);
            String field14 = "age";
            fields.add(field14);
            String field15 = "nc";
            fields.add(field15);
            String field16 = "mainMenu";
            fields.add(field16);


            Class<?> class1 = CreateAccount.class;
            types.add(class1);
            Class<?> class2 = JLabel.class;
            types.add(class2);
            Class<?> class3 = JTextField.class;
            types.add(class3);
            Class<?> class4 = JLabel.class;
            types.add(class4);
            Class<?> class5 = JTextField.class;
            types.add(class5);
            Class<?> class8 = JLabel.class;
            types.add(class8);
            Class<?> class9 = JButton.class;
            types.add(class9);
            Class<?> class10 = JFrame.class;
            types.add(class10);
            Class<?> class11 = JLabel.class;
            types.add(class11);
            Class<?> class12 = JTextField.class;
            types.add(class12);
            Class<?> class13 = int.class;
            types.add(class13);
            Class<?> class14 = JComboBox.class;
            types.add(class14);
            Class<?> class15 = NetworkClient.class;
            types.add(class15);
            Class<?> class16 = JButton.class;
            types.add(class16);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                if (fields.get(i).equals("frame")) {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `public`!",
                            Modifier.isPublic(modifiers));
                } else {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!"
                            , Modifier.isPrivate(modifiers));
                }

                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //createAccountFieldDeclarationTesting

        @Test(timeout = 1000)
        public void mainMenuClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = MainMenu.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `MainMenu` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `MainMenu` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `MainMenu` extends `JComponent`!", JComponent.class, superclass);
            Assert.assertEquals("Ensure that `MainMenu` implements Runnable!", 1, superinterfaces.length);
        } //mainMenuClassDeclarationTest

        @Test(timeout = 1_000)
        public void mainMenuParameterizedConstructorDeclarationTest() {

            Class<?> clazz = MainMenu.class;
            String className = "MainMenu";

            Constructor<?> constructor;
            int modifiers;

            try {
                constructor = clazz.getDeclaredConstructor(NetworkClient.class);
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();

            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
        } //mainMenuParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void mainMenuFieldDeclarationTestOne() {

            Class<?> clazz = MainMenu.class;
            String className = "MainMenu";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "mainMenu";
            fields.add(field1);
            String field2 = "createAccount";
            fields.add(field2);
            String field3 = "logIn";
            fields.add(field3);
            String field4 = "frame";
            fields.add(field4);
            String field5 = "nc";
            fields.add(field5);

            Class<?> class1 = MainMenu.class;
            types.add(class1);
            Class<?> class2 = JButton.class;
            types.add(class2);
            Class<?> class3 = JButton.class;
            types.add(class3);
            Class<?> class4 = JFrame.class;
            types.add(class4);
            Class<?> class5 = NetworkClient.class;
            types.add(class5);

            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                if (fields.get(i).equals("frame")) {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `public`!",
                            Modifier.isPublic(modifiers));
                } else {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!"
                            , Modifier.isPrivate(modifiers));
                }

                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //mainMenuFieldDeclarationTesting

        @Test(timeout = 1000)
        public void invalidCommandClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = InvalidCommandException.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `InvalidCommandException` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `InvalidCommandException` is NOT `abstract`!",
                    Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `InvalidCommandException` extends `Exception`!", Exception.class,
                    superclass);
            Assert.assertEquals("Ensure that `InvalidCommandException` implements no interfaces!", 0,
                    superinterfaces.length);
        } //invalidCommandClassDeclarationTest

        @Test(timeout = 1_000)
        public void invalidCommandParameterizedConstructorDeclarationTest() {

            Class<?> clazz = InvalidCommandException.class;
            String className = "InvalidCommandException";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            int modifiers;
            int modifiers2;

            try {
                constructor2 = clazz.getDeclaredConstructor(String.class);
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
        } //invalidCommandParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void invalidCommandFieldDeclarationTestOne() {

            Class<?> clazz = InvalidCommandException.class;
            String className = "InvalidCommandException";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "serialVersionUID";
            fields.add(field1);


            Class<?> class1 = long.class;
            types.add(class1);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //invalidCommandFieldDeclarationTesting

        @Test(timeout = 1000)
        public void invalidPermissionClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = InvalidPermissionException.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `InvalidPermissionException` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `InvalidPermissionException` is NOT `abstract`!",
                    Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `InvalidPermissionException` extends `Exception`!", Exception.class,
                    superclass);
            Assert.assertEquals("Ensure that `InvalidPermissionException` implements no interfaces!", 0,
                    superinterfaces.length);
        } //invalidPermissionClassDeclarationTest

        @Test(timeout = 1_000)
        public void invalidPermissionParameterizedConstructorDeclarationTest() {

            Class<?> clazz = InvalidPermissionException.class;
            String className = "InvalidPermissionException";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            int modifiers;
            int modifiers2;

            try {
                constructor2 = clazz.getDeclaredConstructor(String.class);
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
        } //invalidPermissionParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void invalidPermissionFieldDeclarationTestOne() {

            Class<?> clazz = InvalidPermissionException.class;
            String className = "InvalidPermissionException";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "serialVersionUID";
            fields.add(field1);


            Class<?> class1 = long.class;
            types.add(class1);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //invalidPermissionFieldDeclarationTesting

        @Test(timeout = 1000)
        public void streamFailedClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = StreamFailedException.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `StreamFailedException` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `StreamFailedException` is NOT `abstract`!",
                    Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `StreamFailedException` extends `Exception`!", Exception.class,
                    superclass);
            Assert.assertEquals("Ensure that `StreamFailedException` implements no interfaces!", 0,
                    superinterfaces.length);
        } //streamFailedClassDeclarationTest

        @Test(timeout = 1_000)
        public void streamFailedParameterizedConstructorDeclarationTest() {

            Class<?> clazz = StreamFailedException.class;
            String className = "StreamFailedException";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            int modifiers;
            int modifiers2;

            try {
                constructor2 = clazz.getDeclaredConstructor(String.class);
                constructor = clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
        } //streamFailedParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void streamFailedFieldDeclarationTestOne() {

            Class<?> clazz = StreamFailedException.class;
            String className = "StreamFailedException";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "serialVersionUID";
            fields.add(field1);


            Class<?> class1 = long.class;
            types.add(class1);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);
            }
        } //streamFailedFieldDeclarationTesting

        @Test(timeout = 1000)
        public void clientThreadClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = ClientThread.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `ClientThread` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `ClientThread` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `ClientThread` extends `Thread`!", Thread.class, superclass);
            Assert.assertEquals("Ensure that `ClientThread` implements no interfaces!", 0, superinterfaces.length);
        } //clientThreadClassDeclarationTest

        @Test(timeout = 1_000)
        public void clientThreadParameterizedConstructorDeclarationTest() {

            Class<?> clazz = ClientThread.class;
            String className = "ClientThread";

            Constructor<?> constructor;

            int modifiers;

            try {
                constructor = clazz.getDeclaredConstructor(Socket.class);
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares its constructor as `public` and it has the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers = constructor.getModifiers();

            Assert.assertTrue("Ensure that all `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
        } //clientThreadParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void clientThreadFieldDeclarationTestOne() {

            Class<?> clazz = ClientThread.class;
            String className = "ClientThread";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "clientSocket";
            fields.add(field1);
            String field2 = "in";
            fields.add(field2);
            String field3 = "out";
            fields.add(field3);
            String field4 = "username";
            fields.add(field4);
            String field5 = "NTFY_LOGGED_IN";
            fields.add(field5);
            String field6 = "NTFY_SIGNED_UP";
            fields.add(field6);
            String field7 = "NTFY_LOGGED_OUT";
            fields.add(field7);
            String field8 = "CLSE_SERVER";
            fields.add(field8);
            String field9 = "SIGN_UP";
            fields.add(field9);
            String field10 = "LOG_IN";
            fields.add(field10);
            String field11 = "DUPL_USER";
            fields.add(field11);
            String field12 = "STRM_FAIL";
            fields.add(field12);
            String field13 = "INVAL_LOGIN";
            fields.add(field13);
            String field14 = "NOT_FOUND";
            fields.add(field14);
            String field16 = "SEND_MSG";
            fields.add(field16);
            String field17 = "EDIT_MSG";
            fields.add(field17);
            String field18 = "DELETE_MSG";
            fields.add(field18);
            String field19 = "CREATE_GRP";
            fields.add(field19);
            String field20 = "CREATE_DM";
            fields.add(field20);


            Class<?> class1 = Socket.class;
            types.add(class1);
            Class<?> class2 = BufferedReader.class;
            types.add(class2);
            Class<?> class3 = PrintWriter.class;
            types.add(class3);
            Class<?> class4 = String.class;
            types.add(class4);
            Class<?> class5 = String.class;
            types.add(class5);
            Class<?> class6 = String.class;
            types.add(class6);
            Class<?> class7 = String.class;
            types.add(class7);
            Class<?> class8 = String.class;
            types.add(class8);
            Class<?> class9 = String.class;
            types.add(class9);
            Class<?> class10 = String.class;
            types.add(class10);
            Class<?> class11 = String.class;
            types.add(class11);
            Class<?> class12 = String.class;
            types.add(class12);
            Class<?> class13 = String.class;
            types.add(class13);
            Class<?> class14 = String.class;
            types.add(class14);
            Class<?> class16 = String.class;
            types.add(class16);
            Class<?> class17 = String.class;
            types.add(class17);
            Class<?> class18 = String.class;
            types.add(class18);
            Class<?> class19 = String.class;
            types.add(class19);
            Class<?> class20 = String.class;
            types.add(class20);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //clientThreadFieldDeclarationTesting

        @Test(timeout = 1000)
        public void messageTestFunc() {
            Message message = new Message("Sample", "Hi");
            String className = "Message";

            String resultGetMessage = message.getMessage();

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "getMessage" + "` method returns the correct " +
                    "value!", resultGetMessage, "Hi");

            message.editMessage("Bye");
            resultGetMessage = message.getMessage();

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "editMessage" + "` method returns the correct" +
                    " value!", resultGetMessage, "Bye");

            String sender = message.getSenderUsername();
            LocalDateTime localDateTime = message.getTimeStamp();

            Assert.assertNotNull("Ensure that `" + className + "`'s `" + "getTimeStamp" + "` method returns the " +
                    "correct value!", localDateTime);

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "getSenderUsername" + "` method returns the " +
                    "correct value!", sender, "Sample");
        } //messageTestFunc

        @Test(timeout = 1000)
        public void clientThreadMethodDeclarationTesting() {

            Class<?> clazz = ClientThread.class;
            String className = "ClientThread";

            Method testMethod;
            int modifiers;
            Class<?> type;

            ArrayList<String> methodNames = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();
            ArrayList<Class<?>[]> inputClasses = new ArrayList<>();

            String method1 = "getUsername";
            methodNames.add(method1);
            String method2 = "getOut";
            methodNames.add(method2);
            String method3 = "initializeUser";
            methodNames.add(method3);
            String method4 = "handleUser";
            methodNames.add(method4);
            String method5 = "serializeUser";
            methodNames.add(method5);
            String method7 = "stopClient";
            methodNames.add(method7);


            inputClasses.add(null);
            inputClasses.add(null);
            Class<?>[] input = new Class[1];
            input[0] = String.class;
            inputClasses.add(input);
            Class<?>[] input3 = new Class[1];
            input3[0] = String.class;
            inputClasses.add(input3);
            Class<?>[] input1 = new Class[1];
            input1[0] = User.class;
            inputClasses.add(input1);
            inputClasses.add(null);


            Class<?> class1 = String.class;
            types.add(class1);
            Class<?> class2 = PrintWriter.class;
            types.add(class2);
            Class<?> class3 = void.class;
            types.add(class3);
            Class<?> class4 = void.class;
            types.add(class4);
            Class<?> class5 = String.class;
            types.add(class5);
            Class<?> class7 = void.class;
            types.add(class7);


            for (int i = 0; i < methodNames.size(); i++) {
                try {
                    testMethod = clazz.getDeclaredMethod(methodNames.get(i), inputClasses.get(i));
                } catch (NoSuchMethodException e) {
                    Assert.fail("Ensure that `" + className + "` declares a method named `" + methodNames.get(i) +
                            "`!");

                    continue;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testMethod.getModifiers();

                type = testMethod.getReturnType();

                if (methodNames.get(i).equals("handleUser")) {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                            "`private`!", Modifier.isPrivate(modifiers));

                } else {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                            "`public`!", Modifier.isPublic(modifiers));
                }

                Assert.assertEquals("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is the " +
                        "correct type!", expectedType, type);
            }
        } //clientThreadMethodDeclarationTesting

        @Test(timeout = 1000)
        public void databaseClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = Database.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `Database` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `Database` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `Database` extends `Object`!", Object.class, superclass);
            Assert.assertEquals("Ensure that `Database` implements no interfaces!", 0, superinterfaces.length);
        } //databaseClassDeclarationTest

        @Test(timeout = 1000)
        public void databaseFieldDeclarationTestOne() {

            Class<?> clazz = Database.class;
            String className = "Database";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "userData";
            fields.add(field1);
            String field2 = "liveUsers";
            fields.add(field2);


            Class<?> class1 = HashMap.class;
            types.add(class1);
            Class<?> class2 = LinkedList.class;
            types.add(class2);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //databaseFieldDeclarationTesting

        @Test(timeout = 1000)
        public void databaseMethodDeclarationTesting() {

            Class<?> clazz = Database.class;
            String className = "Database";

            Method testMethod;
            int modifiers;
            Class<?> type;

            ArrayList<String> methodNames = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();
            ArrayList<Class<?>[]> inputClasses = new ArrayList<>();

            String method1 = "initializeData";
            methodNames.add(method1);
            String method2 = "writeData";
            methodNames.add(method2);
            String method3 = "signUp";
            methodNames.add(method3);
            String method4 = "login";
            methodNames.add(method4);
            String method5 = "signOut";
            methodNames.add(method5);
            String method6 = "sendMessage";
            methodNames.add(method6);
            String method7 = "editMessage";
            methodNames.add(method7);
            String method8 = "removeMessage";
            methodNames.add(method8);
            String method9 = "sendChatToUser";
            methodNames.add(method9);
            String method10 = "userNameAvailable";
            methodNames.add(method10);

            inputClasses.add(null);
            inputClasses.add(null);
            Class<?>[] input3 = new Class[4];
            input3[0] = String.class;
            input3[1] = String.class;
            input3[2] = int.class;
            input3[3] = String.class;
            inputClasses.add(input3);
            Class<?>[] input4 = new Class[2];
            input4[0] = String.class;
            input4[1] = String.class;
            inputClasses.add(input4);
            Class<?>[] input5 = new Class[1];
            input5[0] = String.class;
            inputClasses.add(input5);
            Class<?>[] input6 = new Class[3];
            input6[0] = String.class;
            input6[1] = UUID.class;
            input6[2] = String.class;
            inputClasses.add(input6);
            Class<?>[] input7 = new Class[4];
            input7[0] = String.class;
            input7[1] = UUID.class;
            input7[2] = UUID.class;
            input7[3] = String.class;
            inputClasses.add(input7);
            Class<?>[] input8 = new Class[3];
            input8[0] = String.class;
            input8[1] = UUID.class;
            input8[2] = UUID.class;
            inputClasses.add(input8);
            Class<?>[] input9 = new Class[2];
            input9[0] = String.class;
            input9[1] = Chat.class;
            inputClasses.add(input9);
            Class<?>[] input10 = new Class[1];
            input10[0] = String.class;
            inputClasses.add(input10);


            Class<?> class1 = void.class;
            types.add(class1);
            Class<?> class2 = void.class;
            types.add(class2);
            Class<?> class3 = User.class;
            types.add(class3);
            Class<?> class4 = User.class;
            types.add(class4);
            Class<?> class5 = void.class;
            types.add(class5);
            Class<?> class6 = void.class;
            types.add(class6);
            Class<?> class7 = void.class;
            types.add(class7);
            Class<?> class8 = void.class;
            types.add(class8);
            Class<?> class9 = void.class;
            types.add(class9);
            Class<?> class10 = boolean.class;
            types.add(class10);


            for (int i = 0; i < methodNames.size(); i++) {
                try {
                    testMethod = clazz.getDeclaredMethod(methodNames.get(i), inputClasses.get(i));
                } catch (NoSuchMethodException e) {
                    Assert.fail("Ensure that `" + className + "` declares a method named `" + methodNames.get(i) +
                            "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testMethod.getModifiers();

                type = testMethod.getReturnType();

                if (methodNames.get(i).equals("sendChatToUser") || methodNames.get(i).equals("userNameAvailable")) {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                            "`private`!", Modifier.isPrivate(modifiers));
                } else {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                            "`public`!", Modifier.isPublic(modifiers));
                }

                Assert.assertEquals("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is the " +
                        "correct type!", expectedType, type);
            }
        } //databaseMethodDeclarationTesting

        @Test(timeout = 1000)
        public void serverControllerClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = ServerController.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `ServerController` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `ServerController` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `ServerController` extends `Object`!", Object.class, superclass);
            Assert.assertEquals("Ensure that `ServerController` implements no interfaces!", 0, superinterfaces.length);
        } //serverControllerClassDeclarationTest

        @Test(timeout = 1000)
        public void serverControllerFieldDeclarationTestOne() {

            Class<?> clazz = ServerController.class;
            String className = "ServerController";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "server";
            fields.add(field1);
            String field2 = "PORT_NUMBER";
            fields.add(field2);
            String field3 = "SRVR_OPEN";
            fields.add(field3);
            String field4 = "SRVR_CLOSE";
            fields.add(field4);
            String field5 = "SRVR_ERR";
            fields.add(field5);
            String field6 = "SRVR_CLOSE_ERR";
            fields.add(field6);
            String field7 = "CLNT_CNCT";
            fields.add(field7);
            String field8 = "CLNT_DISCNCT";
            fields.add(field8);
            String field9 = "CLNT_ACCPT_ERR";
            fields.add(field9);
            String field10 = "connectedUsers";
            fields.add(field10);
            String field11 = "stopRequest";
            fields.add(field11);

            Class<?> class1 = ServerSocket.class;
            types.add(class1);
            Class<?> class2 = int.class;
            types.add(class2);
            Class<?> class3 = String.class;
            types.add(class3);
            Class<?> class4 = String.class;
            types.add(class4);
            Class<?> class5 = String.class;
            types.add(class5);
            Class<?> class6 = String.class;
            types.add(class6);
            Class<?> class7 = String.class;
            types.add(class7);
            Class<?> class8 = String.class;
            types.add(class8);
            Class<?> class9 = String.class;
            types.add(class9);
            Class<?> class10 = List.class;
            types.add(class10);
            Class<?> class11 = boolean.class;
            types.add(class11);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!",
                        Modifier.isPrivate(modifiers));
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);

            }
        } //serverControllerFieldDeclarationTesting

        @Test(timeout = 1000)
        public void groupTestFunc() {
            String className = "Group";

            Group group = new Group("Name", new LinkedList<>(), new HashMap<>());

            String currName = group.getGroupName();
            group.addParticipant("User");

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "getGroupName" + "` method returns the " +
                    "correct value!", currName, "Name");
            Assert.assertTrue("Ensure that `" + className + "`'s `" + "getGroupName" + "` method returns the correct " +
                    "value!", group.getParticipants().size() >= 1);


            group.setGroupName("Yo");
            group.removeParticipant("User");
            Assert.assertFalse("Ensure that `" + className + "`'s `" + "getGroupName" + "` method returns the correct" +
                    " value!", group.getParticipants().size() >= 1);

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "setGroupName" + "` method returns the " +
                    "correct value!", group.getGroupName(), "Yo");
        } //groupTestFunc

        @Test(timeout = 1000)
        public void userTestFunc() {
            User user = new User("Sample", "Name", 13, "password");
            String className = "User";

            String resultName = user.getName();

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "getName" + "` method returns the correct " +
                    "value!", resultName, "Name");

            int ageResult = user.getAge();

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "getAge" + "` method returns the correct " +
                    "value!", ageResult, 13);

            String resultUsername = user.getUsername();

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "getUsername" + "` method returns the correct" +
                    " value!", resultUsername, "Sample");

            user.setAge(14);

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "setAge" + "` method returns the correct " +
                    "value!", user.getAge(), 14);

            user.setName("Bob");

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "setName" + "` method returns the correct " +
                    "value!", user.getName(), "Bob");

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "getPassword" + "` method returns the correct" +
                    " value!", user.getPassword(), "password");

            user.setPassword("pass2");

            HashMap<UUID, Chat> chats = user.getChats();

            Assert.assertNotNull("Ensure that `" + className + "`'s `" + "getChats" + "` method returns the correct " +
                    "value!", chats);

            Assert.assertEquals("Ensure that `" + className + "`'s `" + "setPassword" + "` method returns the correct" +
                    " value!", user.getPassword(), "pass2");
        } //userTestFunc

        @Test(timeout = 1000)
        public void serverControllerMethodDeclarationTesting() {

            Class<?> clazz = ServerController.class;
            String className = "ServerController";

            Method testMethod;
            int modifiers;
            Class<?> type;

            ArrayList<String> methodNames = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();
            ArrayList<Class<?>[]> inputClasses = new ArrayList<>();

            String method1 = "addUser";
            methodNames.add(method1);
            String method2 = "getConnectedUsers";
            methodNames.add(method2);
            String method3 = "removeUser";
            methodNames.add(method3);
            String method4 = "notify";
            methodNames.add(method4);
            String method5 = "close";
            methodNames.add(method5);
            String method6 = "start";
            methodNames.add(method6);

            Class<?>[] input1 = new Class[1];
            input1[0] = ClientThread.class;
            inputClasses.add(input1);
            inputClasses.add(null);
            Class<?>[] input3 = new Class[1];
            input3[0] = ClientThread.class;
            inputClasses.add(input3);
            Class<?>[] input4 = new Class[1];
            input4[0] = String.class;
            inputClasses.add(input4);
            inputClasses.add(null);
            inputClasses.add(null);


            Class<?> class1 = void.class;
            types.add(class1);
            Class<?> class2 = List.class;
            types.add(class2);
            Class<?> class3 = boolean.class;
            types.add(class3);
            Class<?> class4 = void.class;
            types.add(class4);
            Class<?> class5 = boolean.class;
            types.add(class5);
            Class<?> class6 = void.class;
            types.add(class6);


            for (int i = 0; i < methodNames.size(); i++) {
                try {
                    testMethod = clazz.getDeclaredMethod(methodNames.get(i), inputClasses.get(i));
                } catch (NoSuchMethodException e) {
                    Assert.fail("Ensure that `" + className + "` declares a method named `" + methodNames.get(i) +
                            "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testMethod.getModifiers();

                type = testMethod.getReturnType();

                if (methodNames.get(i).equals("start")) {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                            "`private`!", Modifier.isPrivate(modifiers));

                } else {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                            "`public`!", Modifier.isPublic(modifiers));

                }

                Assert.assertEquals("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is the " +
                        "correct type!", expectedType, type);
            }
        } //serverControllerMethodDeclarationTesting

        @Test(timeout = 1000)
        public void messagingGuiClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = MessagingGUI.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `MessagingGUI` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `MessagingGUI` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `MessagingGUI` extends `JComponent`!", JComponent.class, superclass);
            Assert.assertEquals("Ensure that `MessagingGUI` implements Runnable!", 1, superinterfaces.length);
        } //messagingGuiDeclarationTest

        @Test(timeout = 1_000)
        public void messagingGuiParameterizedConstructorDeclarationTest() {

            Class<?> clazz = MessagingGUI.class;
            String className = "MessagingGUI";

            Constructor<?> constructor2;
            int modifiers2;

            try {
                constructor2 = clazz.getDeclaredConstructor(NetworkClient.class);
            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");

                return;
            } //end try catch

            modifiers2 = constructor2.getModifiers();

            Assert.assertTrue("Ensure that all `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));
        } //messagingGuiParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void messagingGuiFieldDeclarationTestOne() {

            Class<?> clazz = MessagingGUI.class;
            String className = "MessagingGUI";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "currentMessage";
            fields.add(field1);
            String field2 = "currentChat";
            fields.add(field2);
            String field3 = "frame";
            fields.add(field3);
            String field4 = "chatIcon";
            fields.add(field4);
            String field5 = "chats";
            fields.add(field5);
            String field6 = "scroll";
            fields.add(field6);
            String field7 = "scroller";
            fields.add(field7);
            String field8 = "msg";
            fields.add(field8);
            String field9 = "content";
            fields.add(field9);
            String field10 = "blueOutline";
            fields.add(field10);
            String field11 = "messenger";
            fields.add(field11);
            String field12 = "type";
            fields.add(field12);
            String field13 = "send";
            fields.add(field13);
            String field14 = "text";
            fields.add(field14);
            String field15 = "nc";
            fields.add(field15);
            String field16 = "messages";
            fields.add(field16);
            String field17 = "top";
            fields.add(field17);
            String field18 = "controlPanel";
            fields.add(field18);
            String field19 = "createGroup";
            fields.add(field19);
            String field20 = "createDM";
            fields.add(field20);
            String field21 = "inviteToGroup";
            fields.add(field21);
            String field22 = "viewAccount";
            fields.add(field22);
            String field23 = "signout";
            fields.add(field23);
            String field24 = "leaveGroup";
            fields.add(field24);


            Class<?> class1 = Message.class;
            types.add(class1);
            Class<?> class2 = Chat.class;
            types.add(class2);
            Class<?> class3 = JFrame.class;
            types.add(class3);
            Class<?> class4 = JButton.class;
            types.add(class4);
            Class<?> class5 = JPanel.class;
            types.add(class5);
            Class<?> class6 = JScrollPane.class;
            types.add(class6);
            Class<?> class7 = JScrollPane.class;
            types.add(class7);
            Class<?> class8 = JButton.class;
            types.add(class8);
            Class<?> class9 = Container.class;
            types.add(class9);
            Class<?> class10 = Border.class;
            types.add(class10);
            Class<?> class11 = JPanel.class;
            types.add(class11);
            Class<?> class12 = JPanel.class;
            types.add(class12);
            Class<?> class13 = JButton.class;
            types.add(class13);
            Class<?> class14 = JTextField.class;
            types.add(class14);
            Class<?> class15 = NetworkClient.class;
            types.add(class15);
            Class<?> class16 = JPanel.class;
            types.add(class16);
            Class<?> class17 = JPanel.class;
            types.add(class17);
            Class<?> class18 = JPanel.class;
            types.add(class18);
            Class<?> class19 = JButton.class;
            types.add(class19);
            Class<?> class20 = JButton.class;
            types.add(class20);
            Class<?> class21 = JButton.class;
            types.add(class21);
            Class<?> class22 = JButton.class;
            types.add(class22);
            Class<?> class23 = JButton.class;
            types.add(class23);
            Class<?> class24 = JButton.class;
            types.add(class24);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                if (fields.get(i).equals("currentMessage") || fields.get(i).equals("currentChat")) {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `public`!",
                            Modifier.isPublic(modifiers));
                } else {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!"
                            , Modifier.isPrivate(modifiers));
                }

                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);
            }
        } //messagingGuiFieldDeclarationTesting

        @Test(timeout = 1000)
        public void messagingGuiMethodDeclarationTesting() {

            Class<?> clazz = MessagingGUI.class;
            String className = "MessagingGUI";

            Method testMethod;
            int modifiers;
            Class<?> type;

            ArrayList<String> methodNames = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();
            ArrayList<Class<?>[]> inputClasses = new ArrayList<>();

            String method1 = "refreshDisplay";
            methodNames.add(method1);
            String method2 = "refreshChats";
            methodNames.add(method2);
            String method3 = "refreshMessages";
            methodNames.add(method3);

            inputClasses.add(null);
            inputClasses.add(null);
            inputClasses.add(null);


            Class<?> class1 = void.class;
            types.add(class1);
            Class<?> class2 = void.class;
            types.add(class2);
            Class<?> class3 = void.class;
            types.add(class3);


            for (int i = 0; i < methodNames.size(); i++) {
                try {
                    testMethod = clazz.getDeclaredMethod(methodNames.get(i), inputClasses.get(i));
                } catch (NoSuchMethodException e) {
                    Assert.fail("Ensure that `" + className + "` declares a method named `" + methodNames.get(i) +
                            "`!");

                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testMethod.getModifiers();

                type = testMethod.getReturnType();

                Assert.assertTrue("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is " +
                        "`public`!", Modifier.isPublic(modifiers));

                Assert.assertEquals("Ensure that `" + className + "`'s `" + methodNames.get(i) + "` method is the " +
                        "correct type!", expectedType, type);
            }
        } //messagingGuiMethodDeclarationTesting

        @Test(timeout = 1000)
        public void infoGuiClassDeclarationTest() {
            Class<?> clazz;
            int modifiers;
            Class<?> superclass;
            Class<?>[] superinterfaces;

            clazz = InfoGUI.class;

            modifiers = clazz.getModifiers();

            superclass = clazz.getSuperclass();

            superinterfaces = clazz.getInterfaces();

            Assert.assertTrue("Ensure that `InfoGUI` is `public`!", Modifier.isPublic(modifiers));
            Assert.assertFalse("Ensure that `InfoGUI` is NOT `abstract`!", Modifier.isAbstract(modifiers));
            Assert.assertEquals("Ensure that `InfoGUI` extends `JComponent`!", JComponent.class, superclass);
            Assert.assertEquals("Ensure that `InfoGUI` implements Runnable!", 1, superinterfaces.length);
        } //infoGuiClassDeclarationTest

        @Test(timeout = 1_000)
        public void infoGuiParameterizedConstructorDeclarationTest() {

            Class<?> clazz = InfoGUI.class;
            String className = "InfoGUI";

            Constructor<?> constructor;
            Constructor<?> constructor2;
            int modifiers;
            int modifiers2;

            try {
                constructor = clazz.getDeclaredConstructor(NetworkClient.class);
                constructor2 = clazz.getDeclaredConstructor(NetworkClient.class, Chat.class, String.class);

            } catch (NoSuchMethodException e) {
                Assert.fail("Ensure that `" + className + "` declares all constructors as `public` and they have the " +
                        "correct parameters!");
                return;
            } //end try catch

            modifiers = constructor.getModifiers();
            modifiers2 = constructor2.getModifiers();


            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers));
            Assert.assertTrue("Ensure that both `" + className + "`'s parameterized constructors are `public`!",
                    Modifier.isPublic(modifiers2));

        } //infoGuiParameterizedConstructorDeclarationTest

        @Test(timeout = 1000)
        public void infoGuiFieldDeclarationTestOne() {

            Class<?> clazz = InfoGUI.class;
            String className = "InfoGUI";

            Field testField;
            int modifiers;
            Class<?> type;

            ArrayList<String> fields = new ArrayList<>();
            ArrayList<Class<?>> types = new ArrayList<>();

            String field1 = "nc";
            fields.add(field1);
            String field2 = "id";
            fields.add(field2);
            String field3 = "message";
            fields.add(field3);
            String field4 = "chat";
            fields.add(field4);
            String field5 = "user";
            fields.add(field5);
            String field6 = "option";
            fields.add(field6);
            String field7 = "info";
            fields.add(field7);
            String field8 = "msg";
            fields.add(field8);
            String field9 = "delete";
            fields.add(field9);
            String field10 = "commit";
            fields.add(field10);
            String field11 = "theMessageLabel";
            fields.add(field11);
            String field12 = "theMessage";
            fields.add(field12);
            String field13 = "nameLabel";
            fields.add(field13);
            String field14 = "name";
            fields.add(field14);
            String field15 = "ageLabel";
            fields.add(field15);
            String field16 = "passwordLabel";
            fields.add(field16);
            String field17 = "password";
            fields.add(field17);
            String field18 = "frame";
            fields.add(field18);
            String field19 = "ageGet";
            fields.add(field19);
            String field20 = "agef";
            fields.add(field20);


            Class<?> class1 = NetworkClient.class;
            types.add(class1);
            Class<?> class2 = UUID.class;
            types.add(class2);
            Class<?> class3 = Message.class;
            types.add(class3);
            Class<?> class4 = Chat.class;
            types.add(class4);
            Class<?> class5 = User.class;
            types.add(class5);
            Class<?> class6 = int.class;
            types.add(class6);
            Class<?> class7 = InfoGUI.class;
            types.add(class7);
            Class<?> class8 = MessagingGUI.class;
            types.add(class8);
            Class<?> class9 = JButton.class;
            types.add(class9);
            Class<?> class10 = JButton.class;
            types.add(class10);
            Class<?> class11 = JLabel.class;
            types.add(class11);
            Class<?> class12 = JTextField.class;
            types.add(class12);
            Class<?> class13 = JLabel.class;
            types.add(class13);
            Class<?> class14 = JTextField.class;
            types.add(class14);
            Class<?> class15 = JLabel.class;
            types.add(class15);
            Class<?> class16 = JLabel.class;
            types.add(class16);
            Class<?> class17 = JTextField.class;
            types.add(class17);
            Class<?> class18 = JFrame.class;
            types.add(class18);
            Class<?> class19 = int.class;
            types.add(class19);
            Class<?> class20 = JComboBox.class;
            types.add(class20);


            for (int i = 0; i < fields.size(); i++) {
                try {
                    testField = clazz.getDeclaredField(fields.get(i));
                } catch (NoSuchFieldException e) {
                    Assert.fail("Ensure that `" + className + "` declares a field named `" + fields.get(i) + "`!");
                    return;
                } //end try catch

                Class<?> expectedType = types.get(i);

                modifiers = testField.getModifiers();

                type = testField.getType();

                if (fields.get(i).equals("frame")) {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `public`!",
                            Modifier.isPublic(modifiers));
                } else {
                    Assert.assertTrue("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is `private`!"
                            , Modifier.isPrivate(modifiers));
                }
                Assert.assertEquals("Ensure that `" + className + "`'s `" + fields.get(i) + "` field is the correct " +
                        "type!", expectedType, type);
            }
        } //infoGuiFieldDeclarationTesting
    }
}