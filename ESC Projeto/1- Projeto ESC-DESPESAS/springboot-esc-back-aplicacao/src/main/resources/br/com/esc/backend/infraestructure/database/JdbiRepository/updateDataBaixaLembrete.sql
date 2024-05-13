UPDATE
   tbd_CadastroLembretes
SET
   ds_DataInicial = :data
WHERE
   id_Lembrete = :idLembrete
   AND id_Funcionario = :idFuncionario
