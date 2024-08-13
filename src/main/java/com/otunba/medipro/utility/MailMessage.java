package com.otunba.medipro.utility;

public class MailMessage {

    public static String getVerificationMessage(String name, String link) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Email Verification</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 0;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            height: 100vh;
                        }
                        .container {
                            background-color: #ffffff;
                            border-radius: 8px;
                            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
                            padding: 20px;
                            text-align: center;
                            width: 300px;
                        }
                        h1 {
                            color: #4CAF50;
                            font-size: 24px;
                            margin-bottom: 20px;
                        }
                        p {
                            color: #555;
                            margin-bottom: 20px;
                        }
                        .verify-button {
                            background-color: #4CAF50;
                            color: white;
                            border: none;
                            border-radius: 5px;
                            padding: 10px 20px;
                            font-size: 16px;
                            cursor: pointer;
                            text-decoration: none;
                            display: inline-block;
                        }
                        .verify-button:hover {
                            background-color: #45a049;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 12px;
                            color: #888;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>MediPro Email Verification</h1>
                        <p>Welcome %s !, thank you for registering! Please verify your email address to continue.</p>
                        <a href="%s" class="verify-button">Verify Email</a>
                        <div class="footer">
                            <p>If you did not create an account, no further action is required.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(name, link);
    }

    public static String getPasswordResetMessage(String name, String link) {
        String forgotPasswordEmail = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Forgot Password</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: auto;\n" +
                "            background: white;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "\n" +
                "        .header {\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .content {\n" +
                "            padding: 20px;\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            font-size: 24px;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.5;\n" +
                "            margin: 10px 0;\n" +
                "        }\n" +
                "\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 10px 20px;\n" +
                "            margin: 20px 0;\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "\n" +
                "        .footer {\n" +
                "            text-align: center;\n" +
                "            padding: 20px;\n" +
                "            font-size: 14px;\n" +
                "            color: #777;\n" +
                "        }\n" +
                "\n" +
                "        @media (max-width: 600px) {\n" +
                "            .container {\n" +
                "                width: 100%;\n" +
                "                margin: 0;\n" +
                "            }\n" +
                "\n" +
                "            h1 {\n" +
                "                font-size: 20px;\n" +
                "            }\n" +
                "\n" +
                "            p {\n" +
                "                font-size: 14px;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class=\"container\">\n" +
                "    <div class=\"header\">\n" +
                "        <h1>Reset Your Password</h1>\n" +
                "    </div>\n" +
                "    <div class=\"content\">\n" +
                "        <p>Hello " + name + "!,</p>\n" +
                "        <p>We received a request to reset your password. Click the button below to create a new password:</p>\n" +
                "        <a href=\""+ link + "\" class=\"button\">Reset Password</a>\n" +
                "        <p>If you did not request a password reset, please ignore this email.</p>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "        <p>Thank you,</p>\n" +
                "        <p>MediPro Service Team</p>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    return forgotPasswordEmail;

    }
}
