SELECT
    id_Emprestimo
FROM
    tbd_Emprestimo
WHERE
    ds_TituloEmprestimo = :dsTituloEmprestimo
    AND id_Funcionario = :idFuncionario
