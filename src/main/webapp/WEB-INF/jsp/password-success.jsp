
<%@ page contentType="text/html;charset=UTF-8"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Success Notification</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .dialog {
            background: white;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            padding: 20px;
            text-align: center;
            width: 300px;
        }

        .dialog h2 {
            color: #4CAF50;
            margin: 0;
        }

        .dialog p {
            color: #555;
            margin: 10px 0 20px;
        }

        .checkmark {
            font-size: 50px;
            color: #4CAF50;
            margin-bottom: 10px;
        }

        .button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            font-weight: 500;
        }

        .button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>

<div class="dialog">
    <div class="checkmark">&#10003;</div>
    <h2>Success!</h2>
    <p>${message}.</p>
    <a href="#" class="button">OK</a>
</div>

</body>
</html>