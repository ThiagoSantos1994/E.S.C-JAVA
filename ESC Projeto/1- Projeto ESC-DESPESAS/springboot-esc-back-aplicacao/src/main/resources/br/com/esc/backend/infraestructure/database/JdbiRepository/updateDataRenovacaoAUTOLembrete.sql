UPDATE
   tbd_CadastroLembretes
SET
   ds_DataInicial = CONVERT(VARCHAR, DATEADD(MONTH, 1, :dataInicial), 111)
WHERE
    id_Lembrete = :idLembrete
    AND id_Funcionario = :idFuncionario