
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Reset Password</title>
    <style>
        /* Global Styles */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f2f2f2;
        }

        /* Container Styles */
        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        /* Card Styles */
        .card {
            background-color: white;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
        }

        /* Form Styles */
        form {
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: bold;
            margin-bottom: 10px;
        }

        input[type="password"] {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 3px;
            margin-bottom: 20px;
        }

        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }

        button:hover {
            background-color: #45a049;
        }

        /* Responsive Styles */
        @media (max-width: 600px) {
            .card {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <div class="card">
        <h1>Reset Your Password</h1>
        <form action="${pageContext.request.contextPath}/auth/register/reset-password" method="post">
            <input type="hidden" name="token" value="${token}"/>
            <label for="newPassword">New Password:</label>
            <input type="password" name="newPassword" required id="newPassword">
            <button type="submit">Reset Password</button>
        </form>
    </div>
</div>
</body>
</html>
