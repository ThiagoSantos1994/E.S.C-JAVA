SELECT
    UPPER(ds_Nome) as nomeFuncionario
FROM
    tbd_CadastroFuncionarios
WHERE
    id_Funcionario = :idFuncionario
    AND tp_FuncionarioExcluido = 'N'