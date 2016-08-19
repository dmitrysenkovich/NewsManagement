PROPERTIES_FILE_NAME = 'localization_ru.properties'
JSON_FILE_NAME = 'localization_ru.json'


properties = open(PROPERTIES_FILE_NAME)
json = open(JSON_FILE_NAME, 'w')
json.write('{\n')
for line in properties:
    if line == '\n':
        json.write(line)
    else:
        key, value = line[:-1].split(' = ')
        print(key)
        print(value)
        json.write('    \"' + key + '\": ' + '\"' + value + '\"\n')
json.write('}')
json.close()
properties.close()
